package tn.pedialink.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "otps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    private String id;

    private String email;

    private String code;

    private OtpPurpose purpose;

    private Instant expiresAt;

    @Builder.Default
    private boolean used = false;
}
