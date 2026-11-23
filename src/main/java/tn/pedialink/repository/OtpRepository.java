package tn.pedialink.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.pedialink.model.Otp;
import tn.pedialink.model.OtpPurpose;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findTopByEmailAndPurposeAndUsedFalseOrderByExpiresAtDesc(String email, OtpPurpose purpose);
    void deleteAllByEmailAndPurpose(String email, OtpPurpose purpose);
}
