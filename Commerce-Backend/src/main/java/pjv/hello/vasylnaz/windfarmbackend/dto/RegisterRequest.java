package pjv.hello.vasylnaz.windfarmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import pjv.hello.vasylnaz.windfarmbackend.entity.Role;

public record RegisterRequest(

    @NotBlank(message = "email is required")
    String email,

    @NotBlank(message = "password is required")
    String password,

    @NotBlank(message = "role is required")
    Role role,

    @NotBlank(message = "firstName is required")
    String firstName,

    @NotBlank(message = "lastName is required")
    String lastName,

    String phoneNumber

) {}
