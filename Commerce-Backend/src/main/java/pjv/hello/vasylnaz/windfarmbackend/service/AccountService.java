package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.RegisterRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account registerAccount(RegisterRequest request) {
        if(accountRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email already in use");
        }

        Account account = new Account();
        account.setEmail(request.email());

        String hashedPassword = passwordEncoder.encode(request.password());
        account.setPassword(hashedPassword);

        account.setFirstName(request.firstName());
        account.setLastName(request.lastName());
        account.setRole(request.role());

        if(request.phoneNumber() != null){
            if(accountRepository.existsByPhoneNumber(request.phoneNumber())){
                throw new RuntimeException("Phone number already in use");
            }
            account.setPhoneNumber(request.phoneNumber());
        }

        return accountRepository.save(account);
    }
}
