package pl.wat.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wat.demo.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
