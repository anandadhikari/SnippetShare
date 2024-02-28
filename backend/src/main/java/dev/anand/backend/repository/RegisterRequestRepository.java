package dev.anand.backend.repository;

import dev.anand.backend.entity.RegisterRequest;
import dev.anand.backend.entity.TokenType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRequestRepository extends MongoRepository<RegisterRequest, String> {
    Optional<RegisterRequest> findByVerificationTokenAndVerifiedFalseAndTokenType(String verificationToken, TokenType tokenType);

}
