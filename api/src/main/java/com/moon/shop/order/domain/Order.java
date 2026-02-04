    package com.moon.shop.order.domain;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "orders")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long orderId;
        private String orderNumber;
        private String status;
        private LocalDateTime createdAt;

        @PostPersist
        private void assignOrderNumber() {
            this.orderNumber = "ORD-" + this.orderId;
        }
    }
