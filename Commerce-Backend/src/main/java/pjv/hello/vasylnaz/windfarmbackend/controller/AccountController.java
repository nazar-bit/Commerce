package pjv.hello.vasylnaz.windfarmbackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pjv.hello.vasylnaz.windfarmbackend.dto.AccountResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.AuthResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.LoginRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.RegisterRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.service.AccountService;
import pjv.hello.vasylnaz.windfarmbackend.service.JwtService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

   private final AccountService accountService;
   private final JwtService jwtService;

   public AccountController(AccountService accountService, JwtService jwtService) {
      this.accountService = accountService;
      this.jwtService = jwtService;
   }

   @PreAuthorize("hasAnyRole('CUSTOMER', 'MAINTAINER')")
   @GetMapping("/{id}")
   public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
      return ResponseEntity.ok(accountService.findById(id));
   }

   @PostMapping("/login")
   public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
      AccountResponse account = accountService.login(loginRequest);
      String token = jwtService.generateToken(account.email());
      return ResponseEntity.ok(new AuthResponse(token, account));
   }

   @PostMapping("/register")
   public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
      Account newAccount = accountService.registerAccount(request);
      AccountResponse accountResponse = accountService.mapToResponse(newAccount);

      String token = jwtService.generateToken(newAccount.getEmail());
      return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, accountResponse));
   }
}