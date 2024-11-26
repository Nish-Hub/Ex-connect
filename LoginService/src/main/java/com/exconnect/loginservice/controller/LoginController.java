package com.exconnect.loginservice.controller;

import com.exconnect.loginservice.dto.databasedtos.UserDTO;
import com.exconnect.loginservice.dto.requestdtos.UserAuthenticationRequestDTO;
import com.exconnect.loginservice.dto.requestdtos.UserRegistrationRequestDTO;
import com.exconnect.loginservice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/authorise")
    public ResponseEntity<UserDTO> authorise(UserAuthenticationRequestDTO userRequestDTO) {

        UserDTO userDTO = this.loginService.authenticate(userRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);

    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(UserRegistrationRequestDTO userRequestDTO) {

        UserDTO userDTO = this.loginService.register(userRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK).build();

    }

}
