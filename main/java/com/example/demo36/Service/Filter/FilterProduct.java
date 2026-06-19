package com.example.demo36.Service.Filter;

import com.example.demo36.Entity.Product;
import com.example.demo36.Search.RsqlVoidableFilter;

import java.util.Set;

public class FilterProduct extends RsqlVoidableFilter<Product, Long> {
    // Chỉ được lọc động (RSQL) trên những field này.
    private static final Set<String> QUERYABLE = Set.of(
            "name", "sku", "price", "categorycode"
    );

    public FilterProduct() {
        setWhitelist(QUERYABLE);
    }
}
