package com.moon.shop.common.exception;

import java.util.List;

public class OutOfStockException extends RuntimeException {
    private final List<String> outOfStockProducts;

    public OutOfStockException(String message, List<String> outOfStockProducts) {
        super(message);
        this.outOfStockProducts = outOfStockProducts;
    }

    public List<String> getOutOfStockProducts() {
        return outOfStockProducts;
    }
}
