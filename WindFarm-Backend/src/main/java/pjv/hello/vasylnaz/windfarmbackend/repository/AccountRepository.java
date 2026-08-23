package pjv.hello.vasylnaz.windfarmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByEmail(String email);
}
