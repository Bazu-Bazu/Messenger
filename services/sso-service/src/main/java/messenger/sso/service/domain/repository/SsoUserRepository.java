package messenger.sso.service.domain.repository;

import messenger.sso.service.domain.entity.SsoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsoUserRepository extends JpaRepository<SsoUser, Long> {

    Optional<SsoUser> findByPhone(String phone);
}
