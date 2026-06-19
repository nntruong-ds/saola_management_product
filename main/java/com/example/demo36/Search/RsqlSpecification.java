package com.example.demo36.Search;

import org.springframework.data.jpa.domain.Specification;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import jakarta.persistence.criteria.*;
import vn.saolasoft.base.exception.APIException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RsqlSpecification<T> implements Specification<T> {
    private final ComparisonNode node; // chua thong tin name, key, value
    private final Set<String> whitelist;  // chua cac cot duoc looc

    public RsqlSpecification(ComparisonNode node, Set<String> whitelist) {
        this.node = node;
        this.whitelist = whitelist;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        String field = node.getSelector(); // lay ra ten truong
        // khong duoc tim cac cot ngoai danh sach
        if (!whitelist.contains(field)) {
            throw new APIException("cot khong duoc querry " + field);
        }

        Path<?> path = root.get(field); // root la db, va truy cap vao cot can querry
        Class<?> type = path.getJavaType();        // lay kieu du lieu cua cot
        List<String> args = node.getArguments();   // lay gia tri tu url luon luon la string
        String op = node.getOperator().getSymbol(); // toan tu ==, !=, >, <, >=, <=, =in=, =out=...

        switch (op) {
            case "==": {   // equal, nếu có '*' thì LIKE (không phân biệt hoa thường)
                String arg = args.get(0);
                if (arg.contains("*")) {
                    return cb.like(cb.lower(root.get(field).as(String.class)),
                            arg.replace('*', '%').toLowerCase());
                }
                return cb.equal(path, cast(type, arg));
            }
            case "!=": {  // not equal, nếu có '*' thì NOT LIKE (không phân biệt hoa thường)
                String arg = args.get(0);
                if (arg.contains("*")) {
                    return cb.notLike(cb.lower(root.get(field).as(String.class)),
                            arg.replace('*', '%').toLowerCase());
                }
                return cb.notEqual(path, cast(type, arg));
            }
            case "=gt=": case ">":
                return cb.greaterThan((Expression) path, (Comparable) cast(type, args.get(0)));
            case "=ge=": case ">=":
                return cb.greaterThanOrEqualTo((Expression) path, (Comparable) cast(type, args.get(0)));
            case "=lt=": case "<":
                return cb.lessThan((Expression) path, (Comparable) cast(type, args.get(0)));
            case "=le=": case "<=":
                return cb.lessThanOrEqualTo((Expression) path, (Comparable) cast(type, args.get(0)));
            case "=in=": {
                List<Object> values = args.stream().map(a -> cast(type, a)).collect(Collectors.toList());
                return path.in(values);
            }
            case "=out=": {
                List<Object> values = args.stream().map(a -> cast(type, a)).collect(Collectors.toList());
                return cb.not(path.in(values));
            }
            default:
                throw new APIException("Unsupported operator: " + op);
        }
    }

    // Ép chuỗi client gửi sang đúng kiểu (wrapper) của cột. Sai định dạng → ném 400
    private Object cast(Class<?> type, String value) {
        try {
            if (type == String.class)                       return value;
            if (type == BigDecimal.class)                   return new BigDecimal(value);
            if (type == Integer.class || type == int.class) return Integer.valueOf(value);
            if (type == Long.class    || type == long.class) return Long.valueOf(value);
            if (type == Double.class  || type == double.class) return Double.valueOf(value);
            if (type == Boolean.class || type == boolean.class) return Boolean.valueOf(value);
            if (type == LocalDate.class)                    return LocalDate.parse(value);
            if (type == LocalDateTime.class)                return LocalDateTime.parse(value);
            if (type == Instant.class)                      return Instant.parse(value);
            if (type == UUID.class)                         return UUID.fromString(value);
            if (type.isEnum())                              return Enum.valueOf((Class<? extends Enum>) type, value);
            return value;
        } catch (Exception e) {
            throw new APIException("Invalid value '" + value + "' for field '" + node.getSelector() + "'");
        }
    }
}
