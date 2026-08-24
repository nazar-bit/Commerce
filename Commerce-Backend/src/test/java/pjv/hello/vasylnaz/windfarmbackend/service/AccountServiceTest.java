package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.RegisterRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.Role;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;


    @Test
    void registerAccount() {
        RegisterRequest request = new RegisterRequest(
                "test@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                "-345-87-65"
        );

        accountService.registerAccount(request);

        Account registered = accountRepository.findByEmail("test@gmail.com")
                .orElseThrow(()->new RuntimeException("Account not found"));

        assertEquals("test@gmail.com", registered.getEmail());
        assertTrue(passwordEncoder.matches("A powerful password", registered.getPassword()));
        assertEquals(Role.CUSTOMER, registered.getRole());
        assertEquals("Tester", registered.getFirstName());
        assertEquals("Testeron", registered.getLastName());
        assertEquals("-345-87-65", registered.getPhoneNumber());
    }


    @Test
    void registerAccountNoPhoneNumber() {
        RegisterRequest request = new RegisterRequest(
                "test@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                null
        );

        accountService.registerAccount(request);

        Account registered = accountRepository.findByEmail("test@gmail.com")
                .orElseThrow(()->new RuntimeException("Account not found"));

        assertEquals("test@gmail.com", registered.getEmail());
        assertTrue(passwordEncoder.matches("A powerful password", registered.getPassword()));
        assertEquals(Role.CUSTOMER, registered.getRole());
        assertEquals("Tester", registered.getFirstName());
        assertEquals("Testeron", registered.getLastName());
        assertNull(registered.getPhoneNumber());
    }


    @Test
    void addTwoAccountsWithTheSameEmail(){
        RegisterRequest request1 = new RegisterRequest(
                "test@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                "-345-87-65"
        );

        RegisterRequest request2 = new RegisterRequest(
                "test@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                "-345-87-65"
        );

        accountService.registerAccount(request1);
        assertThrows(RuntimeException.class,
                ()->accountService.registerAccount(request2));
    }


    @Test
    void addTwoAccountsWithTheSamePhoneNumber(){
        RegisterRequest request1 = new RegisterRequest(
                "testA@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                "-345-87-65"
        );

        RegisterRequest request2 = new RegisterRequest(
                "testB@gmail.com",
                "A powerful password",
                Role.CUSTOMER,
                "Tester",
                "Testeron",
                "-345-87-65"
        );

        accountService.registerAccount(request1);
        assertThrows(RuntimeException.class,
                ()->accountService.registerAccount(request2));
    }
}
