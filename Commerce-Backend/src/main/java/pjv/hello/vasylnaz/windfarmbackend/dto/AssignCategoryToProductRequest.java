package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotNull;

public record AssignCategoryToProductRequest(
        @NotNull(message = "productId is required")
        Long productId,

        @NotNull(message = "categoryId is required")
        Long categoryId
) {}
