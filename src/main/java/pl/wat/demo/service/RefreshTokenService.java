package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.demo.dto.IssuedRefresh;
import pl.wat.demo.exception.InvalidRefreshTokenException;
import pl.wat.demo.model.RefreshToken;
import pl.wat.demo.model.User;
import pl.wat.demo.repository.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    public class TokenGenerator {
        private final SecureRandom random = new SecureRandom();
        public String generate() {
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }
    }

    public class TokenHasher {
        public String sha256Hex(String raw) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder(digest.length * 2);
                for (byte b : digest) sb.append(String.format("%02x", b));
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private final int refreshInterval = 10000;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHasher tokenHasher = new TokenHasher();
    private final TokenGenerator tokenGenerator = new TokenGenerator();

    @Transactional
    public IssuedRefresh issueRefresh(User user){
        // revoke

        String raw = tokenGenerator.generate();
        String hash = tokenHasher.sha256Hex(raw);

        RefreshToken refreshToken = new RefreshToken()
                .setUser(user)
                .setTokenHash(hash)
                .setExpiresAt(LocalDateTime.now().plusSeconds(refreshInterval));
        refreshTokenRepository.save(refreshToken);

        return new IssuedRefresh(raw, refreshToken);
    }

    private void checkExpiredOrRevoked(RefreshToken refreshToken){
        if(refreshToken.isExpired()){
            throw new InvalidRefreshTokenException("Expired token");
        }

        if(refreshToken.isRevoked()){
            throw new InvalidRefreshTokenException("Revoked token");
        }
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        String hash = tokenHasher.sha256Hex(token);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token"));

        checkExpiredOrRevoked(refreshToken);

        return refreshToken;
    }

    @Transactional
    public IssuedRefresh consumeAndRotate(String token) {
        RefreshToken currentToken = validate(token);

        currentToken.setRevokedAt(LocalDateTime.now());
        IssuedRefresh newToken = issueRefresh(currentToken.getUser());
        currentToken.setReplacedBy(newToken.refreshToken());

        return newToken;
    }

    @Transactional
    public void revoke(String rawToken) {
        String hash = tokenHasher.sha256Hex(rawToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> rt.setRevokedAt(LocalDateTime.now()));
    }

    //sprzatanie starych tokenow
}
