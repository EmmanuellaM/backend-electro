package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByEmailAndToken(String email, String token);

    void deleteByEmail(String email);
}
