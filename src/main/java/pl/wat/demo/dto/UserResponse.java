package pl.wat.demo.dto;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    boolean isAdmin
) {
}
