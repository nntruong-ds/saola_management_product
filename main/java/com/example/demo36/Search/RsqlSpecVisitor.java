package com.example.demo36.Search;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class RsqlSpecVisitor<T> implements RSQLVisitor<Specification<T>, Void> {
    private final Set<String> whitelist;

    public  RsqlSpecVisitor(Set<String> whitelist) {
        this.whitelist = whitelist;
    }
    // Visitor Pattern
    @Override
    public Specification<T> visit(AndNode node, Void param) { // AND logic ';'
        return node.getChildren().stream()
                .map(child -> child.accept(this)) // duyet tung node con, va tra ve Specification cho tung node con
                .reduce(Specification::and) // sau khi cac node con chuyen jpa no and
                .orElse(null);
    }

    @Override
    public Specification<T> visit(OrNode node, Void param) { // OR logic ','
        return node.getChildren().stream()
                .map(child -> child.accept(this))
                .reduce(Specification::or)
                .orElse(null);
    }

    @Override
    public Specification<T> visit(ComparisonNode node, Void param) { // Điều kiện đơn (lá của cây)
        return new RsqlSpecification<>(node, whitelist);
    }
}
