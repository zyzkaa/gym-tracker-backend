package pl.wat.demo.dto;

import pl.wat.demo.model.RefreshToken;

public record IssuedRefresh(
        String raw,
        RefreshToken refreshToken
) {
}
