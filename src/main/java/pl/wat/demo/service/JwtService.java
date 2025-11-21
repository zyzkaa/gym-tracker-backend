package pl.wat.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pl.wat.demo.dto.IssuedJwt;
import pl.wat.demo.model.User;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final int accessInterval = 100000;
    private final JwtEncoder jwtEncoder;
    @Value("${auth.jwt.issuer}")
    private String issuer;

    public IssuedJwt issueJwt(User user){
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(accessInterval);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(expires)
                .claim("username", user.getUsername())
                .claim("role", user.getIsAdmin() ? "ADMIN" : "USER")
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        return new IssuedJwt(token, accessInterval);
    }
}
