package com.exconnect.authservice.authprovider;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;

public interface IAuthProvider {

    public String createToken(String userId, String username, String userRole);

    public void validateToken(String token) throws TokenExpiredException, InvalidTokenException;

    public String invalidateToken(String token);
}
