package dev.anand.backend.controller;

import dev.anand.backend.dto.auth.AuthRequest;
import dev.anand.backend.dto.auth.AuthResponse;
import dev.anand.backend.dto.auth.ChangePassword;
import dev.anand.backend.dto.user.UserResponseDto;
import dev.anand.backend.dto.user.UserRequestDto;
import dev.anand.backend.entity.RegisterRequest;
import dev.anand.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        authService.addRegisterRequest(registerRequest);
        return ResponseEntity.ok("Successfully added your register request. Now verify your email!!!");
    }

    @PostMapping("/verify/{token}")
    public ResponseEntity<UserResponseDto> verifyUser(@PathVariable String token, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.verifyRegisterToken(token, userRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authorize(request));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Void> forgetPassword(@RequestParam String email) {
        authService.forgetPasswordRequest(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/new-password")
    public ResponseEntity<Void> forgetPassword(
            @RequestParam String verificationToken,
            @RequestBody ChangePassword changePassword
    ) {
        authService.newPassword(verificationToken, changePassword);
        return ResponseEntity.noContent().build();
    }
}
