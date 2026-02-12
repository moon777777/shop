package com.moon.shop.order.service;

import com.moon.shop.order.domain.Order;
import com.moon.shop.order.domain.OrderItem;
import com.moon.shop.order.domain.OrderStatus;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.common.domain.Address;
import com.moon.shop.order.dto.response.*;
import com.moon.shop.order.exception.OrderNotFoundException;
import com.moon.shop.common.exception.AddressNotFoundException;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.common.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;


    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address with ID " + request.getAddressId() + " not found."));

        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .orderType(request.getOrderType())
                .address(address)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

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
                        totalProductPrice = totalProductPrice.add(item.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                        totalDiscountPrice = totalDiscountPrice.add(
                            item.getProduct().getOriginalPrice().subtract(item.getPriceAtOrder()).multiply(BigDecimal.valueOf(item.getQuantity()))
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
                orderPage.getTotalElements(), // Added totalElements
                orderPage.getTotalPages(),
                orderSummaryResponses
        );
    }

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

        // Map Address entity to OrderAddressResponse
        OrderAddressResponse addressResponse = new OrderAddressResponse(
                order.getAddress().getRecipientName(),
                order.getAddress().getPhoneNumber(),
                order.getAddress().getZipCode(),
                order.getAddress().getAddress1(),
                order.getAddress().getAddress2()
        );


        List<ItemDetailResponse> itemDetailResponses = order.getOrderItems().stream()
                .map(orderItem -> {

                    BigDecimal itemTotalPrice = orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                    return new ItemDetailResponse(
                            orderItem.getProduct().getProductId(),
                            orderItem.getProduct().getName(),
                            orderItem.getProduct().getThumbnailImage(),
                            orderItem.getProduct().getOriginalPrice().intValue(),
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
            totalProductPrice = totalProductPrice.add(item.getProduct().getOriginalPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalDiscountAmount = totalDiscountAmount.add(
                item.getProduct().getOriginalPrice().subtract(item.getPriceAtOrder()).multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
        
        BigDecimal finalPaymentPrice = totalProductPrice.subtract(totalDiscountAmount).add(deliveryFee);

        return new OrderDetailResponse(
                order.getOrderId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                itemDetailResponses,
                addressResponse,
                totalProductPrice.intValue(),
                totalDiscountAmount.intValue(),
                deliveryFee.intValue(),
                finalPaymentPrice.intValue(),
                order.getCreatedAt().toString()
        );
    }


}
