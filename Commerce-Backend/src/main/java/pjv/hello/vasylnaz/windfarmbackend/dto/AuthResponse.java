package pjv.hello.vasylnaz.windfarmbackend.dto;

public record AuthResponse(
        String token,
        AccountResponse account
) {}