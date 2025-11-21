package pl.wat.demo.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wat.demo.dto.IssuedJwt;
import pl.wat.demo.dto.IssuedRefresh;
import pl.wat.demo.dto.TokenResponse;
import pl.wat.demo.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TokenMapper {
    UserMapper userMapper;

    public TokenResponse toResponse(IssuedJwt issuedJwt, IssuedRefresh issuedRefresh, User user) {
        return new TokenResponse(
                userMapper.toResponse(user),
                "Bearer",
                issuedJwt.token(),
                issuedJwt.expiresIn(),
                issuedRefresh.raw(),
                Duration.between(LocalDateTime.now(), issuedRefresh.refreshToken().getExpiresAt()).getSeconds()
        );
    }
}
