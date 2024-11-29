package com.exconnect.authservice.controller;

import com.exconnect.authservice.exceptions.RetriesExhaustedException;
import com.exconnect.authservice.service.AuthService;
import com.exconnect.dto.ApiResponse;
import com.exconnect.dto.TokenRequest;
import com.exconnect.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

// TODO : Need to decide how to take the api version from the user
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/tokens")
    public Mono<ResponseEntity<ApiResponse>> generateToken(@RequestBody @Valid UserDTO userDTO) {
        log.info("Request received to generate token for user: {}", userDTO.getUserId());

        return Mono.fromCallable(() -> this.authService.generateToken(userDTO))
                .map(token -> {
                    log.info("Token successfully generated for user: {}", userDTO.getUserId());
                    return ResponseEntity.ok(ApiResponse.builder().message(null).responseData(token).build());
                }).timeout(Duration.ofSeconds(20))
                .retryWhen(Retry
                        .backoff(3,Duration.ofMillis(500))
                        .maxBackoff(Duration.ofSeconds(5))
                        .jitter(0.3)
                        .filter(e -> e instanceof TimeoutException)
                        .doBeforeRetry(retrySignal -> log.info("Initiating {} time(s) retry for generate token due to: {}"
                                ,retrySignal.totalRetries()+1,retrySignal.failure().getMessage()))
                        .doAfterRetry(retrySignal -> log.info("Completed retry for generate token attempt: {}",
                                retrySignal.totalRetries()+1))
                        .onRetryExhaustedThrow((retrySpec, retrySignal) -> new RetriesExhaustedException("Retries exhausted")));

    }

    @PostMapping("/v1/tokens/validate")
    public Mono<ResponseEntity<ApiResponse>> validateToken(@RequestBody @Valid TokenRequest tokenRequest) {
        log.info("Request received to validate token");

        return Mono.fromCallable(() -> {
                    this.authService.validateToken(tokenRequest.getToken());
                    return ResponseEntity.ok(ApiResponse.builder().message("Token valid").responseData(null).build());
                }).timeout(Duration.ofSeconds(20))
                .retryWhen(Retry
                        .backoff(3,Duration.ofMillis(500))
                        .maxBackoff(Duration.ofSeconds(5))
                        .jitter(0.3)
                        .filter(e -> e instanceof TimeoutException)
                        .doBeforeRetry(retrySignal -> log.info("Initiating {} time(s) retry for validate token due to: {}"
                                ,retrySignal.totalRetries()+1,retrySignal.failure().getMessage()))
                        .doAfterRetry(retrySignal -> log.info("Completed retry for validate token attempt: {}",
                                retrySignal.totalRetries()+1))
                        .onRetryExhaustedThrow((retrySpec, retrySignal) -> new RetriesExhaustedException("Retries exhausted")));


    }

    @PostMapping("/v1/tokens/invalidateToken")
    public Mono<ResponseEntity<ApiResponse>> invalidateToken(@RequestBody @Valid TokenRequest tokenRequest) {
        log.info("Request received to invalidate token");

        return Mono.fromCallable(
                        () -> this.authService.invalidateToken(tokenRequest.getToken()))
                .map(expiredToken -> {
                    log.info("Token successfully invalidated for user");
                    return ResponseEntity.ok(ApiResponse.builder().message(null).responseData(expiredToken).build());
                })
                .timeout(Duration.ofSeconds(20))
                .retryWhen(Retry
                        .backoff(3,Duration.ofMillis(500))
                        .maxBackoff(Duration.ofSeconds(5))
                        .jitter(0.3)
                        .filter(e -> e instanceof TimeoutException)
                        .doBeforeRetry(retrySignal -> log.info("Initiating {} time(s) retry for invalidate token due to: {}"
                                ,retrySignal.totalRetries()+1,retrySignal.failure().getMessage()))
                        .doAfterRetry(retrySignal -> log.info("Completed retry for invalidate token attempt: {}",
                                retrySignal.totalRetries()+1))
                        .onRetryExhaustedThrow((retrySpec, retrySignal) -> new RetriesExhaustedException("Retries exhausted")));

    }

}
