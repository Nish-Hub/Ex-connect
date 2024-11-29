package com.exconnect.authservice.controller.handler;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.RetriesExhaustedException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.exception.pojo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ErrorResponse> handleInterruptedException(InterruptedException exception){
        log.error("Interrupted exception encountered",exception);
        ErrorResponse interruptedExceptionErrorResponse = new ErrorResponse("Operation interrupted", "ERR001"
        , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(interruptedExceptionErrorResponse);
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<ErrorResponse> handleExecutionException(ExecutionException exception){
        log.error("Execution exception encountered",exception);
        ErrorResponse executionExceptionErrorResponse = new ErrorResponse("Execution exception", "ERR002"
                , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(executionExceptionErrorResponse);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpirationException(TokenExpiredException exception){
        log.error("TokenExpired exception encountered",exception);
        ErrorResponse tokenExpiredExceptionErrorResponse = new ErrorResponse("Token Expired Exception", "ERR003"
                , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(tokenExpiredExceptionErrorResponse);

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception){
        log.error("InvalidToken exception encountered",exception);
        ErrorResponse invalidTokenExceptionErrorResponse = new ErrorResponse("InvalidTokenException "+exception.getMessage(), "ERR004"
                , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(invalidTokenExceptionErrorResponse);
    }

    @ExceptionHandler({TimeoutException.class})
    public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException exception){
        log.error("Timeout exception encountered",exception);
        ErrorResponse timeoutExceptionErrorResponse = new ErrorResponse("TimeoutException "+exception.getMessage(), "ERR005"
                , LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(timeoutExceptionErrorResponse);
    }

    @ExceptionHandler(RetriesExhaustedException.class)
    public ResponseEntity<ErrorResponse> handleRetriesExhaustedException(RetriesExhaustedException exception) {
        log.error("Retries exhausted", exception);
        ErrorResponse response = new ErrorResponse(
                "RetriesExhaustedException: " + exception.getMessage(),
                "ERR006",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        log.error("Unexpected exception encountered", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                "ERR999",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
