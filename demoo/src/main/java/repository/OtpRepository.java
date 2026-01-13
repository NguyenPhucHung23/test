package Practice.repository;

import Practice.model.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findByEmail(String email);

    Optional<OtpEntity> findByOtpCode(String otpCode);

    Optional<OtpEntity> findByResetToken(String resetToken);

    void deleteByEmail(String email);
}
