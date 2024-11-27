package com.exconnect.authservice.authprovider;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.entities.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTAuthProviderTest {

    @Autowired
    private JWTAuthProvider jwtAuthProvider;

    UserDTO userDTO;

    @BeforeEach
    void setUp() {

         userDTO = UserDTO.builder()
                .userId("1234")
                .userName("Nishant")
                .userRoles(List.of("admin,user"))
                .build();


    }

    @Test
    void createToken() {

        String token = jwtAuthProvider.createToken(userDTO);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");

        // Decode the token
        Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtAuthProvider.secretKey)
                                .build()
                                        .parseClaimsJws(token)
                                                .getBody();


        assertTrue(token.split("\\.").length == 3, "Token should follow the JWT format");
        assertEquals(userDTO.getUserId(),claims.getSubject(),"Subject should match the user ID");
        assertEquals(userDTO.getUserName(),claims.get("username"),"username should match the user ");
        assertEquals(userDTO.getUserRoles(),claims.get("role", List.class),"role should match the user ");


        // Validate expiration
        assertNotNull(claims.getExpiration(),"Token should have an expiration date");
        assertTrue(claims.getExpiration().after(new Date()), "Token expiration should be after now");

    }

    @Test
    void shouldNotThrowExceptionForValidToken() {
        String validToken = jwtAuthProvider.createToken(userDTO);
        assertDoesNotThrow(() -> jwtAuthProvider.validateToken(validToken), "Valid token should not throw an exception");
    }

    @Test
    void shouldThrowTokenExpiredExceptionForExpiredToken() throws InterruptedException {
        String expiredToken = jwtAuthProvider.createToken(userDTO, 1); // 1 millisecond lifespan
        Thread.sleep(10); // Ensure expiration
        assertThrows(TokenExpiredException.class, () -> jwtAuthProvider.validateToken(expiredToken), "Expired token should throw an exception");
    }

    @Test
    void shouldThrowInvalidTokenExceptionForMalformedToken() {
        String malformedToken = "invalid.token.format";
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.validateToken(malformedToken), "Invalid token should throw an exception");
    }

    @Test
    void shouldThrowInvalidTokenExceptionForEmptyToken() {
        String emptyToken = "";
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.validateToken(emptyToken), "Empty token should throw InvalidTokenException");
    }

    @Test
    void shouldThrowInvalidTokenExceptionForNullToken() {
        String nullToken = null;
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.validateToken(nullToken), "Null token should throw InvalidTokenException");
    }


    @Test
    void invalidateToken() {
    }
}