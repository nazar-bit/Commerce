package pjv.hello.vasylnaz.windfarmbackend.dto;

import java.util.List;

public record ProductResponse(
        Long id,

        String name,

        String description,

        double price,

        List<Long> categories,

        Long instanceCount
) {}
