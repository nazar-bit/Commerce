package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductInstancesRequest(
    @NotBlank(message = "ProductName is required")
    String productName,
    @NotNull(message = "quantity is required")
    int quantity

) {}
