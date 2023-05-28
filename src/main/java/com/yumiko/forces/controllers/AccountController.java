package com.yumiko.forces.controllers;

import com.yumiko.forces.models.Account;
import com.yumiko.forces.repositories.AccountRepo;
import com.yumiko.forces.util.Hashing;
import com.yumiko.forces.util.Token;
import com.yumiko.forces.validation.Validator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepo accountRepo;

    @PostMapping(value = "/register")
    public @ResponseBody ResponseEntity<?> register(@ModelAttribute("account") Account account, HttpSession session) {

        if (account.getEmail() == null || account.getEmail().isEmpty() || !Validator.validateEmail(account.getEmail()) || accountRepo.existsByEmail(account.getEmail()) == 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("email");
        }

        if (account.getPassword() == null || account.getPassword().isEmpty() || !Validator.validatePassword(account.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("password");
        }

        String id = "";

        try {
            id = Hashing.hash(account.getEmail() + Hashing.randomString(10));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

        try {
            accountRepo.save(new Account(id, account.getEmail(), Hashing.hash(account.getPassword())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

        try {

            session.setAttribute("auth", id + "." + Token.generateToken(id));
            return ResponseEntity.status(HttpStatus.OK).body("");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseEntity<String> login(@ModelAttribute("account") Account account, HttpSession session) {

        if (account.getEmail() == null || account.getEmail().isEmpty() || accountRepo.existsByEmail(account.getEmail()) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
        }

        try {
            if (accountRepo.existsByEmailAndPassword(account.getEmail(), Hashing.hash(account.getPassword())) == 0) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("");
            }

            String id = accountRepo.findByEmail(account.getEmail()).getId();
            session.setAttribute("auth", id + "." + Token.generateToken(id));
            return ResponseEntity.status(HttpStatus.OK).body("");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }

    }

}
