package com.example.demo36.Search;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;
import java.util.Set;
public class Queryable {
    private Queryable() {}

    public static <T> Specification<T> toSpecification(String query, Set<String> whitelist) {
        if (query == null || query.isBlank()) return null;
        Node root = new RSQLParser().parse(query); // chuyen thanh cac node

        return root.accept(new RsqlSpecVisitor<>(whitelist));
    }
}
