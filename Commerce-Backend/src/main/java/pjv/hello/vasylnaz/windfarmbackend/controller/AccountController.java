package pjv.hello.vasylnaz.windfarmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjv.hello.vasylnaz.windfarmbackend.dto.AccountResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.RegisterRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController
{
   private final AccountService accountService;

   public AccountController(AccountService accountService) {
      this.accountService = accountService;
   }

   @PostMapping("/register")
   public ResponseEntity<Account> register(@RequestBody RegisterRequest request) {

      Account newAccount = accountService.registerAccount(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
   }

   @GetMapping("/{id}")
   public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
      AccountResponse account = accountService.findById(id);
      return ResponseEntity.ok(account);
   }
}
