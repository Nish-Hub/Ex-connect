package com.exconnect.authservice.authprovider;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;

public interface IAuthProvider<T> {

    public String createToken(T user);

    public void validateToken(String token) throws TokenExpiredException, InvalidTokenException;

    public String invalidateToken(String token);
}
