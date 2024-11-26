package com.exconnect.loginservice.exception.exceptionhandler;

import com.exconnect.loginservice.exception.PasswordNotMatchingException;
import com.exconnect.loginservice.exception.UserNameAlreadyExistsException;
import com.exconnect.loginservice.exception.UserNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginGlobalExceptionHandler {

    @ExceptionHandler({UserNameNotFoundException.class})
    public ResponseEntity<Object> handleUserNameNotFoundException(UserNameNotFoundException userNameNotFoundException) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(userNameNotFoundException.getMessage());
    }

    @ExceptionHandler({PasswordNotMatchingException.class})
    public ResponseEntity<Object> handlePasswordNotMatchingException(PasswordNotMatchingException passwordNotMatchingException) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(passwordNotMatchingException.getMessage());
    }

    @ExceptionHandler({UserNameAlreadyExistsException.class})
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UserNameAlreadyExistsException userNameAlreadyExistsException) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(userNameAlreadyExistsException.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException runtimeException) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(runtimeException.getMessage());
    }

}
