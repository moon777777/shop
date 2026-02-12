package com.moon.shop.order.domain;

public enum OrderStatus {
    PENDING,        // 주문 접수됨
    PROCESSING,     // 처리 중
    SHIPPED,        // 배송 중
    DELIVERED,      // 배송 완료
    CANCELLED,      // 주문 취소됨
    RETURNED        // 반품됨
}
