package com.moon.shop.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<ProductResponse> products;

    public PageResponse(int page,
                        int size,
                        long totalElements,
                        int totalPages,
                        List<ProductResponse> products
    ) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.products = products;
    }



}
