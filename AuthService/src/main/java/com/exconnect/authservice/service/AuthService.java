package com.exconnect.authservice.service;

import com.exconnect.authservice.authprovider.IAuthProvider;
import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthService {


    private IAuthProvider authProvider;

    @Autowired
    public AuthService(@Qualifier("JwtAuth") IAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public String generateToken(Map<String, String> requestMap) {
        String userId = requestMap.get("userId");
        String userName = requestMap.get("userName");
        String userRole = requestMap.get("userRole"); // Assuming we have a single role or multiple roles concatenated

        return this.authProvider.createToken(userId, userName, userRole);
    }

    public void validateToken(String token) throws InvalidTokenException, TokenExpiredException {

        this.authProvider.validateToken(token);
    }

    public String invalidateToken(String token) {

        return this.authProvider.invalidateToken(token);
    }
}
