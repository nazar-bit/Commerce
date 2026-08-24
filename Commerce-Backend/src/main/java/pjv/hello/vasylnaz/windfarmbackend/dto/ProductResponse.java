package pjv.hello.vasylnaz.windfarmbackend.dto;

public record ProductResponse(
        Long id,

        String name,

        String description,

        String imageUrl,

        double price
) {}
