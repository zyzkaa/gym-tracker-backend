package pl.wat.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.demo.dto.LoginRequest;
import pl.wat.demo.dto.TokenResponse;
import pl.wat.demo.dto.RegisterRequest;
import pl.wat.demo.dto.UserResponse;
import pl.wat.demo.service.UserService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> addUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.addUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refreshToken) {
        userService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }
}
