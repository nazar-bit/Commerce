package pjv.hello.vasylnaz.windfarmbackend.dto;

import pjv.hello.vasylnaz.windfarmbackend.entity.Role;


public record AccountResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Role role
) {}
