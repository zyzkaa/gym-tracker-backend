package pl.wat.demo.dto;

public record LoginRequest(
        String email,
        String password
) {
}
