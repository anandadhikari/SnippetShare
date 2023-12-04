package dev.anand.backend.service;

import dev.anand.backend.dto.auth.AuthRequest;
import dev.anand.backend.dto.auth.AuthResponse;
import dev.anand.backend.dto.auth.ChangePassword;
import dev.anand.backend.dto.user.UserResponseDto;
import dev.anand.backend.dto.user.UserRequestDto;
import dev.anand.backend.entity.RegisterRequest;

public interface AuthService {
    void addRegisterRequest(RegisterRequest request);

    UserResponseDto verifyRegisterToken(String token, UserRequestDto userRequestDto);

    AuthResponse authorize(AuthRequest request);

    void forgetPasswordRequest(String email);

    void newPassword(String verificationToken, ChangePassword changePassword);
}
