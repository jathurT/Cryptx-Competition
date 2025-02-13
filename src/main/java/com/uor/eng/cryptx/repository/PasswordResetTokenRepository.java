package com.uor.eng.cryptx.repository;

import com.uor.eng.cryptx.model.PasswordResetToken;
import com.uor.eng.cryptx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {

  void deleteByUser(User user);

  Optional<PasswordResetToken> findByToken(String token);

  Optional<PasswordResetToken> findByUser(User user);
}
