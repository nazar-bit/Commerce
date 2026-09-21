package pjv.hello.vasylnaz.windfarmbackend.seeder;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.entity.Role;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;

@Component
public class AccountSeeder {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountSeeder(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void seed() {

        Account customer = new Account();
        customer.setEmail("customer");
        customer.setFirstName("customer");
        customer.setLastName("customer");
        customer.setPassword(passwordEncoder.encode("customer"));
        customer.setRole(Role.CUSTOMER);

        Account maintainer = new Account();
        maintainer.setEmail("maintainer");
        maintainer.setFirstName("maintainer");
        maintainer.setLastName("maintainer");
        maintainer.setPassword(passwordEncoder.encode("maintainer"));
        maintainer.setRole(Role.MAINTAINER);

        accountRepository.save(customer);
        accountRepository.save(maintainer);
    }
}
