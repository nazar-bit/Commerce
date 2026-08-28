package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull(message = "orderId is required")
    Long orderId
) {}
