package pjv.hello.vasylnaz.windfarmbackend.dto;

import pjv.hello.vasylnaz.windfarmbackend.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        Long accountId,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        LocalDateTime refundedAt
) {}
