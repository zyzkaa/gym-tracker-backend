package pl.wat.demo.mapper;

import org.springframework.stereotype.Component;
import pl.wat.demo.dto.RegisterRequest;
import pl.wat.demo.dto.UserResponse;
import pl.wat.demo.model.User;

@Component
public class UserMapper {
    public User toEntity(RegisterRequest registerRequest) {
        return new User()
                .setUsername(registerRequest.username())
                .setPassword(registerRequest.password())
                .setEmail(registerRequest.email())
                .setIsAdmin(registerRequest.isAdmin())
                .setIsActive(true);
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getIsAdmin());
    }
}
