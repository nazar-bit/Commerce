package pjv.hello.vasylnaz.windfarmbackend.dto;

public record CategoryResponse(
   Long id,
   String name,
   Long superId
) {}
