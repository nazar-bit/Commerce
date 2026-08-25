package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignCategoryToProductRequest(
        @NotBlank(message = "productId is required")
        Long productId,

        @NotBlank(message = "categoryId is required")
        Long categoryId
) {}
