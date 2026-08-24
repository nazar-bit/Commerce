package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
    @NotBlank(message = "AccountId is required")
    Long accountId

) {}
