package com.exconnect.loginservice.service;

import com.exconnect.loginservice.dao.LoginRepository;
import com.exconnect.loginservice.dto.databasedtos.UserDTO;
import com.exconnect.loginservice.dto.requestdtos.UserAuthenticationRequestDTO;
import com.exconnect.loginservice.dto.requestdtos.UserRegistrationRequestDTO;
import com.exconnect.loginservice.exception.UserNameAlreadyExistsException;
import com.exconnect.loginservice.exception.UserNameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class LoginService {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public UserDTO authenticate(UserAuthenticationRequestDTO userRequestDTO) {

        UserDTO userDTO = this.loginRepository
                .findByUserName(userRequestDTO.getUserName());

        if (Objects.isNull(userDTO)) {
            log.info("User not found for username {}", userRequestDTO.getUserName());
            throw new UserNameNotFoundException("User not found for username " + userRequestDTO.getUserName());
        }

        if (!userRequestDTO.getHashPassword().equals(userDTO.getHashPassword())) {
            log.info("password not matching for username {}", userRequestDTO.getUserName());
            throw new UserNameNotFoundException("password not matching for username " + userRequestDTO.getUserName());
        }

        return userDTO;

    }

    public UserDTO register(UserRegistrationRequestDTO userRequestDTO) {
        UserDTO userDTO = this.loginRepository
                .findByUserName(userRequestDTO.getUserName());

        if (Objects.nonNull(userDTO)) {
            log.info("Username already exists for username {}", userRequestDTO.getUserName());
            throw new UserNameAlreadyExistsException("Username already exists for username " + userRequestDTO.getUserName());
        }

        return this.loginRepository.save(userRequestDTO.getUserDto());

    }
}
