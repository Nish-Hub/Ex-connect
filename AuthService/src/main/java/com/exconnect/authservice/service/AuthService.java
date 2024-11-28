package com.exconnect.authservice.service;

import com.exconnect.authservice.authprovider.IAuthProvider;
import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {


    private IAuthProvider authProvider;

    @Autowired
    public AuthService(@Qualifier("JwtAuth") IAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public String generateToken(UserDTO userDTO) {

        return this.authProvider.createToken(UserDTO.builder().userId(userDTO.getUserId())
                .userName(userDTO.getUserName())
                .userRoles(userDTO.getUserRoles()));
    }

    public void validateToken(String token) throws InvalidTokenException, TokenExpiredException {

        this.authProvider.validateToken(token);
    }

    public String invalidateToken(String token) throws InvalidTokenException, TokenExpiredException {

        return this.authProvider.invalidateToken(token);
    }
}
