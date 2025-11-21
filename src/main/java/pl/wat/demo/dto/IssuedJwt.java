package pl.wat.demo.dto;

import java.time.Instant;

public record IssuedJwt(
        String token,
        long expiresIn
) {
}
