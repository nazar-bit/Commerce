package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductInstancesRequest(
    @NotBlank(message = "ProductName is required")
    String productName,
    @NotBlank(message = "quantity is required")
    int quantity

) {}
