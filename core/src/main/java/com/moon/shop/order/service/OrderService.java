package com.moon.shop.order.service;

import com.moon.shop.common.domain.Address;
import com.moon.shop.common.exception.AddressNotFoundException;
import com.moon.shop.common.exception.OutOfStockException;
import com.moon.shop.common.repository.AddressRepository;
import com.moon.shop.order.domain.Order;
import com.moon.shop.order.domain.OrderItem;
import com.moon.shop.order.domain.OrderStatus;
import com.moon.shop.order.domain.OrderType;
import com.moon.shop.order.dto.request.CreateOrderRequest;
import com.moon.shop.order.dto.response.*;
import com.moon.shop.order.exception.OrderNotFoundException;
import com.moon.shop.order.repository.OrderItemRepository;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.product.domain.Product;
import com.moon.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final ProductService productService;

    @Transactional
    public OrderCreateResponse createOrder(CreateOrderRequest request) {
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address with ID " + request.getAddressId() + " not found."));

        List<String> outOfStockProducts = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Items cannot be empty for an order.");
        }

        for (com.moon.shop.order.dto.request.OrderItemRequest orderItemRequest : request.getItems()) {
            Product product = productService.getProductById(orderItemRequest.getProductId());
            if (product.getStock() < orderItemRequest.getQuantity()) {
                outOfStockProducts.add(product.getName());
            } else {
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(orderItemRequest.getQuantity())
                        .priceAtOrder(BigDecimal.valueOf(product.getDiscountPrice()))
                        .build();
                orderItems.add(orderItem);
            }
        }

        if (!outOfStockProducts.isEmpty()) {
            throw new OutOfStockException("Following products are out of stock: " + String.join(", ", outOfStockProducts), outOfStockProducts);
        }
        
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .orderType(OrderType.DIRECT.name())
                .address(address)
                .createdAt(LocalDateTime.now())
                .build();
        
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
            productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return new OrderCreateResponse(
                savedOrder.getOrderId(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus().name(),
                savedOrder.getCreatedAt().toString()
        );
    }

    public OrderListResponse getMyOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderSummaryResponse> orderSummaryResponses = orderPage.getContent().stream()
                .map(order -> {
                    BigDecimal totalProductPrice = BigDecimal.ZERO;
                    BigDecimal totalDiscountPrice = BigDecimal.ZERO;
                    int orderItemCount = 0;

                    for (OrderItem item : order.getOrderItems()) {
                        totalProductPrice = totalProductPrice.add(BigDecimal.valueOf(item.getProduct().getOriginalPrice()).multiply(BigDecimal.valueOf(item.getQuantity())));
                        totalDiscountPrice = totalDiscountPrice.add(
                            BigDecimal.valueOf(item.getProduct().getOriginalPrice()).subtract(item.getPriceAtOrder()).multiply(BigDecimal.valueOf(item.getQuantity()))
                        );
                        orderItemCount += item.getQuantity();
                    }
                    BigDecimal finalPaymentPrice = totalProductPrice.subtract(totalDiscountPrice).add(new BigDecimal("2000"));

                    return new OrderSummaryResponse(
                            order.getOrderId(),
                            order.getOrderNumber(),
                            totalProductPrice.intValue(),
                            totalDiscountPrice.intValue(),
                            finalPaymentPrice.intValue(),
                            order.getStatus().name(),
                            orderItemCount,
                            order.getCreatedAt().toString()
                    );
                })
                .collect(Collectors.toList());

        return new OrderListResponse(
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderSummaryResponses
        );
    }

    @Transactional
    public OrderCreateResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found."));

        order.changeStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return new OrderCreateResponse(
                updatedOrder.getOrderId(),
                updatedOrder.getOrderNumber(),
                updatedOrder.getStatus().name(),
                updatedOrder.getCreatedAt().toString()
        );
    }

    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found."));

        OrderAddressResponse deliveryAddress = new OrderAddressResponse(
                order.getAddress().getRecipientName(),
                order.getAddress().getPhoneNumber(),
                order.getAddress().getZipCode(),
                order.getAddress().getAddress1(),
                order.getAddress().getAddress2()
        );

        List<OrderItemDetail> orderItems = order.getOrderItems().stream()
                .map(orderItem -> {
                    BigDecimal itemTotalPrice = orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                    return new OrderItemDetail(
                            orderItem.getProduct().getId(),
                            orderItem.getProduct().getName(),
                            orderItem.getProduct().getThumbnailImage(),
                            orderItem.getProduct().getOriginalPrice(),
                            orderItem.getPriceAtOrder().intValue(),
                            orderItem.getQuantity(),
                            itemTotalPrice.intValue()
                    );
                })
                .collect(Collectors.toList());

        BigDecimal totalProductPrice = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        BigDecimal deliveryFee = new BigDecimal("2000");

        for (OrderItem item : order.getOrderItems()) {
            totalProductPrice = totalProductPrice.add(BigDecimal.valueOf(item.getProduct().getOriginalPrice()).multiply(BigDecimal.valueOf(item.getQuantity())));
            totalDiscountAmount = totalDiscountAmount.add(
                BigDecimal.valueOf(item.getProduct().getOriginalPrice()).subtract(item.getPriceAtOrder()).multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        BigDecimal finalPaymentPrice = totalProductPrice.subtract(totalDiscountAmount).add(deliveryFee);

        return new OrderDetailResponse(
                order.getOrderId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                orderItems,
                deliveryAddress,
                totalProductPrice.intValue(),
                totalDiscountAmount.intValue(),
                deliveryFee.intValue(),
                finalPaymentPrice.intValue(),
                order.getCreatedAt().toString(),
                order.getPaidAt() != null ? order.getPaidAt().toString() : null
        );
    }
}
