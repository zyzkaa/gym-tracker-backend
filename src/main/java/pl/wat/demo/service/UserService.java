package pl.wat.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.demo.dto.*;
import pl.wat.demo.exception.EmailAlreadyExistsException;
import pl.wat.demo.exception.WrongCredentialsException;
import pl.wat.demo.mapper.TokenMapper;
import pl.wat.demo.mapper.UserMapper;
import pl.wat.demo.model.User;
import pl.wat.demo.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final TokenMapper tokenMapper;

    private void checkUserExists(String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException();
        }
    }

    @Transactional
    public TokenResponse addUser(RegisterRequest registerRequest) {
        checkUserExists(registerRequest.email());

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return login(new LoginRequest(user.getEmail(), registerRequest.password()));
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new WrongCredentialsException("Account with this email does not exist"));

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            throw new WrongCredentialsException("Wrong password");
        }

        IssuedJwt issuedJwt = jwtService.issueJwt(user);
        IssuedRefresh issuedRefresh = refreshTokenService.issueRefresh(user);
        return tokenMapper.toResponse(issuedJwt, issuedRefresh, user);
    }

    public TokenResponse refreshToken(String refreshToken) {
        IssuedRefresh issuedRefresh = refreshTokenService.consumeAndRotate(refreshToken);
        User user = issuedRefresh.refreshToken().getUser();
        IssuedJwt issuedJwt = jwtService.issueJwt(user);
        return tokenMapper.toResponse(issuedJwt, issuedRefresh, user);
    }

    public void logout(String token) {
        refreshTokenService.revoke(token);
    }
}
