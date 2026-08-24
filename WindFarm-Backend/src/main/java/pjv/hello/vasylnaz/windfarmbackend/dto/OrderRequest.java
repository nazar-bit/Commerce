package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
    @NotBlank(message = "orderId is required")
    Long orderId
) {}
