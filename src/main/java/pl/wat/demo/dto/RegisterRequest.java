package pl.wat.demo.dto;

public record RegisterRequest(
        String username,
        String email,
        String password,
        boolean isAdmin
) {
}
