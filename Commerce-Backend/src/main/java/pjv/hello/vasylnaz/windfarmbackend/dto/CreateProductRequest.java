package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.*;

public record CreateProductRequest(

    @NotBlank(message = "Product name is required")
    String name,

    String description,

    String imageUrl,

    @Positive(message = "Price must be strictly greater than zero")
    double price,

    Long categoryId

) {}
