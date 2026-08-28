package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
    @NotNull(message = "AccountId is required")
    Long accountId

) {}
