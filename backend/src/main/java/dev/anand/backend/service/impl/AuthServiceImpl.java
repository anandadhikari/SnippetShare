package dev.anand.backend.service.impl;

import dev.anand.backend.dto.auth.AuthRequest;
import dev.anand.backend.dto.auth.AuthResponse;
import dev.anand.backend.dto.auth.ChangePassword;
import dev.anand.backend.dto.user.UserRequestDto;
import dev.anand.backend.dto.user.UserResponseDto;
import dev.anand.backend.entity.Mail;
import dev.anand.backend.entity.RegisterRequest;
import dev.anand.backend.entity.TokenType;
import dev.anand.backend.entity.User;
import dev.anand.backend.exception.InvalidTokenException;
import dev.anand.backend.exception.NotMatchingException;
import dev.anand.backend.exception.UserNotFoundException;
import dev.anand.backend.repository.RegisterRequestRepository;
import dev.anand.backend.repository.UserRepository;
import dev.anand.backend.service.AuthService;
import dev.anand.backend.service.UserService;
import dev.anand.backend.util.AppUtil;
import dev.anand.backend.util.JWTUtil;
import dev.anand.backend.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RegisterRequestRepository requestRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void addRegisterRequest(RegisterRequest request) {
        AppUtil.validatePassword(request.getPassword());
        AppUtil.validateEmail(request.getEmail());
        request.setId(UUID.randomUUID().toString());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setTokenType(TokenType.REGISTER);
        request.setVerified(false);

        String verificationToken = AppUtil.getEncryptedToken(request);
        request.setVerificationToken(verificationToken);
        requestRepository.save(request);

        // TODO: 20-06-2023 write proper mail appropriate for frontend
        Mail mail = Mail.builder()
                .to(request.getEmail())
                .subject("Email verification !!!")
                .messages("Thank you for Registering in SnippetService.\n\n\n" +
                        "Your verification token : " + verificationToken + "\n" +
                        "\t\tOR\n\n" +
                        "Verify yourself at http://localhost:3000/createaccount/" + verificationToken
                )
                .build();
        MailUtil.sendMail(mail);

    }

    @Override
    public UserResponseDto verifyRegisterToken(String token, UserRequestDto userRequestDto) {
        RegisterRequest request = requestRepository
                .findByVerificationTokenAndVerifiedFalseAndTokenType(token, TokenType.REGISTER)
                .orElseThrow(() -> new InvalidTokenException(
                        "The token provided is malformed or doesn't exist.",
                        List.of("token : " + token,
                                "Token type : " + TokenType.REGISTER,
                                "Cannot verify the token.",
                                "Cannot created the user",
                                "User : " + userRequestDto.toString())

                ));
        User user = User
                .builder()
                .firstname(userRequestDto.getFirstname())
                .lastname(userRequestDto.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        requestRepository.delete(request);
        return userService.createUser(user);
    }

    @Override
    public AuthResponse authorize(AuthRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));


        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Invalid email provided !!!",
                        List.of("email : " + request.getEmail(),
                                "Cannot authorize user.",
                                "Login unsuccessful !!!")
                ));

        return new AuthResponse(jwtUtil.generateToken(user), user.getId());
    }

    @Override
    public void forgetPasswordRequest(String email) {
        System.out.println(email);
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Invalid email provided !!!",
                        List.of("email : " + email,
                                "Cannot find user with this email.",
                                "Forget password unsuccessful !!!")
                ));
        RegisterRequest request = RegisterRequest
                .builder()
                .id(UUID.randomUUID().toString())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(false)
                .tokenType(TokenType.FORGET_PASSWORD)
                .build();
        String verificationToken = AppUtil.getEncryptedToken(request);
        request.setVerificationToken(verificationToken);
        requestRepository.save(request);
        // TODO: 20-06-2023 write proper mail appropriate for frontend
        Mail mail = Mail.builder()
                .to(request.getEmail())
                .subject("Password Reset Request !!!")
                .messages("URL ENDPOINT" +
                        "/?verificationToken=" +
                        verificationToken +
                        "\n If It is not you contact the developers or give feedback about this issue.")
                .build();
        MailUtil.sendMail(mail);
    }

    @Override
    public void newPassword(String verificationToken, ChangePassword changePassword) {
        RegisterRequest request = requestRepository
                .findByVerificationTokenAndVerifiedFalseAndTokenType(verificationToken, TokenType.FORGET_PASSWORD)
                .orElseThrow(() -> new InvalidTokenException(
                        "The token provided is malformed or doesn't exist.",
                        List.of("token : " + verificationToken,
                                "Token type : " + TokenType.FORGET_PASSWORD,
                                "Cannot verify the token.",
                                "Cannot change password",
                                changePassword.toString())

                ));
        AppUtil.validatePassword(changePassword.getNewPassword());
        if (!changePassword.getNewPassword().equals(changePassword.getRe_Password())) {
            throw new NotMatchingException("The fields doesn't match!!!",
                    List.of("newPassword : " + changePassword.getNewPassword(),
                            "re_Password : " + changePassword.getRe_Password(),
                            "The fields doesn't match.",
                            "Retry with the matching field"));
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Invalid email provided !!!",
                        List.of("email : " + request.getEmail(),
                                "Cannot find user with this email.",
                                "Forget password unsuccessful !!!")
                ));
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        requestRepository.delete(request);

        // TODO: 20-06-2023 write proper mail appropriate for frontend
        Mail mail = Mail.builder()
                .to(request.getEmail())
                .subject("Password Reset Request Successful !!!")
                .messages("Your new password is : " + changePassword.getNewPassword())
                .build();
        MailUtil.sendMail(mail);
        userRepository.save(user);
    }
}
