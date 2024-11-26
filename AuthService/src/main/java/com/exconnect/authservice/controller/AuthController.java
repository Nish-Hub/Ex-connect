package com.exconnect.authservice.controller;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.authservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    private AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(Map<String, String> requestMap) {

        try {
            String token = executorService.submit(() -> this.authService.generateToken(requestMap)).get();
            return ResponseEntity.ok(token);
        } catch (InterruptedException e) {
            log.error("Interrupted Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token generation interrupted");
        } catch (ExecutionException e) {
            log.error("Execution Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(String token) {

        try {
            return executorService.submit(() -> {
                try {
                    this.authService.validateToken(token);
                    return ResponseEntity.ok("Token valid");
                } catch (InvalidTokenException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token Invalid");
                } catch (TokenExpiredException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token Expired");
                }
            }).get();
        } catch (InterruptedException e) {
            log.error("Interrupted Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token generation interrupted");
        } catch (ExecutionException e) {
            log.error("Execution Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/invalidateToken")
    public ResponseEntity<String> invalidateToken(String token) {

        try {
            String expiredToken = executorService.submit(() -> this.authService.invalidateToken(token)).get();
            return ResponseEntity.ok(expiredToken);
        } catch (InterruptedException e) {
            log.error("Interrupted Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token generation interrupted");
        } catch (ExecutionException e) {
            log.error("Execution Exception ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
