package com.exconnect.authservice.authprovider;


import com.exconnect.authservice.util.cryptokey.KeyGenerator;
import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.dto.UserDTO;
//import com.exconnect.util.cryptokey.KeyGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component("JwtAuth")
public class JWTAuthProvider implements IAuthProvider<UserDTO> {

    final String secretKey = KeyGenerator.generateSymmetricKey();
    final TokenExpiry tokenExpiry = new TokenExpiry();
    final long expirationTimeInMillis = 1000 * 60 * 60;

    final String validTokenFormat = "[a-zA-Z0-9\\-_.]+";

    public String createToken(UserDTO userDTO) {

       return this.createToken(userDTO,expirationTimeInMillis);
    }

    public String createToken(UserDTO userDTO,long tokenExpirationTimeInMillis) {

        Date now = new Date();
        String jwtToken = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setSubject(userDTO.getUserId())
                .claim("username", userDTO.getUserName())
                .claim("role", userDTO.getUserRoles())
                .setIssuedAt(now)
                .setExpiration(tokenExpiry.createExpiryDate(now,tokenExpirationTimeInMillis))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
        return jwtToken;


    }



    public void validateToken(String token) throws TokenExpiredException, InvalidTokenException {

        this.isValidToken(token);

        int clockSkewSeconds = 60;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .setAllowedClockSkewSeconds(clockSkewSeconds)
                    .build()
                    .parseClaimsJws(token);

            log.info("token is valid");
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Token has expired", expiredJwtException);
            throw new TokenExpiredException("Token Expired", expiredJwtException);
        } catch (Exception exception) {
            log.error("Invalid token", exception);
            throw new InvalidTokenException("Invalid token", exception);
        }

    }

    // TODO:  true invalidation without reissuing tokens, consider token blacklisting or other stateful approaches. This will help to maintain active user list as well
    public String invalidateToken(String token) throws InvalidTokenException, TokenExpiredException {

        this.validateToken(token);

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();

        var claims = jwtParser.parseClaimsJws(token).getBody();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenExpiry.oldDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

    }

    private boolean isValidToken(String token) throws InvalidTokenException {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Token cannot be null or blank");
        }

        // Format validation (e.g., regex for allowed characters)
        if (!token.matches(validTokenFormat)) {
            throw new InvalidTokenException("Token format is invalid");
        }

        return true; // Valid if no exceptions are thrown
    }


    static class TokenExpiry {

        // Represent Jan 1, 2000, in milliseconds since epoch
        long oldDateMillis = 946684800000L; // Jan 1, 2000, 00:00:00 UTC

        Date oldDate = new Date(oldDateMillis);

        public Date createExpiryDate(Date now,long tokenExpirationTime) {

            Date expiryDate = new Date(now.getTime() + tokenExpirationTime);
            return expiryDate;
        }

    }
}
