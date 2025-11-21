package pl.wat.demo.dto;

import java.time.LocalDateTime;

public record TokenResponse(
        UserResponse userInfo,
        String tokenType,
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn
) {
}
