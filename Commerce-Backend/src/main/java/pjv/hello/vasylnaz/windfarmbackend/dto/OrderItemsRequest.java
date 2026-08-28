package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemsRequest (
        @NotNull(message = "orderId is required")
        Long orderId,
        @NotNull(message = "productId is required")
        Long productId,
        @NotNull(message = "quantity is required")
        int quantity
) {}
