package tn.pedialink.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.pedialink.model.Role;
import tn.pedialink.model.User;
import tn.pedialink.model.UserStatus;
import tn.pedialink.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            String adminEmail = "admin@pedialink.tn";

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .fullName("PediaLink Admin")
                        .email(adminEmail)
                        .passwordHash(passwordEncoder.encode("Admin123!"))
                        .role(Role.ADMIN)
                        .status(UserStatus.ACTIVE)
                        .emailVerified(true)
                        .build();

                userRepository.save(admin);
                log.info("Admin account seeded: {}", adminEmail);
            } else {
                log.info("Admin account already exists");
            }
        } catch (Exception e) {
            log.warn("Could not seed admin account (MongoDB may be unreachable): {}", e.getMessage());
        }
    }
}
