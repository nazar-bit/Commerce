package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record UnOrderItemsRequest(
        @NotBlank(message = "orderId is required")
        Long orderId,
        @NotBlank(message = "productId is required")
        Long productId,
        @NotBlank(message = "quantity is required")
        int quantity
) {}
