package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelOrderRequest(
    @NotBlank(message = "orderId is required")
    Long orderId
) {}
