package com.exconnect.authservice.controller;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.authservice.service.AuthService;
import com.exconnect.dto.TokenRequest;
import com.exconnect.dto.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final ExecutorService executorService ;

    @Autowired
    private AuthController(AuthService authService,ExecutorService executorService) {
        this.authService = authService;
        this.executorService = executorService;
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(@RequestBody @Valid UserDTO userDTO) throws ExecutionException, InterruptedException {

            String token = executorService.submit(() -> this.authService.generateToken(userDTO)).get();
            return ResponseEntity.ok(token);

    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody @Valid TokenRequest tokenRequest) throws ExecutionException, InterruptedException {

            return executorService.submit(() -> {
                    this.authService.validateToken(tokenRequest.getToken());
                    return ResponseEntity.ok("Token valid");
            }).get();

    }

    @PostMapping("/invalidateToken")
    public ResponseEntity<String> invalidateToken(@RequestBody @Valid TokenRequest tokenRequest) throws ExecutionException, InterruptedException {

            String expiredToken = executorService.submit(() -> this.authService.invalidateToken(tokenRequest.getToken())).get();
            return ResponseEntity.ok(expiredToken);

    }

}
