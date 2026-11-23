package tn.pedialink.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.pedialink.dto.*;
import tn.pedialink.exception.BadRequestException;
import tn.pedialink.model.*;
import tn.pedialink.repository.UserRepository;
import tn.pedialink.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    public ApiMessage signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        Role role;
        try {
            role = Role.valueOf(req.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be DOCTOR or PARENT");
        }

        if (role == Role.ADMIN) {
            throw new BadRequestException("Cannot self-register as ADMIN");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .phone(req.getPhone())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();

        if (role == Role.DOCTOR) {
            user.setSpecialization(req.getSpecialization());
            user.setLicenseNumber(req.getLicenseNumber());
            user.setClinicName(req.getClinicName());
        }

        userRepository.save(user);

        otpService.generateOtp(req.getEmail(), OtpPurpose.VERIFY_EMAIL);

        return new ApiMessage("Account created. Please verify your email with the OTP sent.");
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new BadRequestException("Account is suspended. Contact support.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public ApiMessage requestOtp(OtpRequest req) {
        OtpPurpose purpose;
        try {
            purpose = OtpPurpose.valueOf(req.getPurpose().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid purpose. Must be VERIFY_EMAIL or RESET_PASSWORD");
        }

        userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("No account found with this email"));

        otpService.generateOtp(req.getEmail(), purpose);

        return new ApiMessage("OTP sent. Check your console (dev mode).");
    }

    public AuthResponse verifyOtp(VerifyOtpRequest req) {
        OtpPurpose purpose;
        try {
            purpose = OtpPurpose.valueOf(req.getPurpose().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid purpose");
        }

        boolean valid = otpService.verifyOtp(req.getEmail(), req.getCode(), purpose);
        if (!valid) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (purpose == OtpPurpose.VERIFY_EMAIL) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public ApiMessage resetPassword(ResetPasswordRequest req) {
        boolean valid = otpService.verifyOtp(req.getEmail(), req.getCode(), OtpPurpose.RESET_PASSWORD);
        if (!valid) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        return new ApiMessage("Password reset successful. You can now log in.");
    }
}
