package com.exconnect.authservice.authprovider;

import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

    // valid invalidate token
    @Test
    void testInvalidateToken_ValidToken_ThrowsTokenExpiredException_InvokingValidateToken() throws InvalidTokenException, TokenExpiredException {
        String validToken = jwtAuthProvider.createToken(userDTO);
        String invalidatedToken = jwtAuthProvider.invalidateToken(validToken);
        assertThrows(TokenExpiredException.class,()->jwtAuthProvider.validateToken(invalidatedToken),"Invalidated token should thrown an expired token exception");
    }

    // expiry should match #TokenExpiry.oldDate
    @Test
    void testInvalidateToken_ValidToken_Success() throws InvalidTokenException, TokenExpiredException {
        String validToken = jwtAuthProvider.createToken(userDTO);

        // Decode the token
        Claims validClaims = Jwts.parserBuilder()
                .setSigningKey(jwtAuthProvider.secretKey)
                .build()
                .parseClaimsJws(validToken)
                .getBody();

        String invalidatedToken = jwtAuthProvider.invalidateToken(validToken);

        // Decode the token
        Claims invalidatedClaims = Jwts.parserBuilder()
                .setSigningKey(jwtAuthProvider.secretKey)
                .build()
                .parseClaimsJws(invalidatedToken)
                .getBody();


        assertTrue(invalidatedToken.split("\\.").length == 3, "Token should follow the JWT format");
        assertClaimsMatch(validClaims,invalidatedClaims);

        // Validate expiration
        assertNotNull(invalidatedClaims.getExpiration(),"Token should have an expiration date");
        assertTrue(invalidatedClaims.getExpiration().equals(new JWTAuthProvider.TokenExpiry().oldDate), "Token expiration should be  same as oldDate");

    }

    private void assertClaimsMatch(Claims expected,Claims actual){
        assertEquals(expected.getSubject(),actual.getSubject(),"Subject should match the user ID");
        assertEquals(expected.get("username"),actual.get("username"),"username should match the user ");
        assertEquals(expected.get("role", List.class),actual.get("role", List.class),"role should match the user ");

    }

    // token already expired , invalidate should throw Token expired exception
    @Test
    void testInvalidateToken_ExpiredToken_ThrowsTokenExpiredException() throws InterruptedException {
        String expiredToken = jwtAuthProvider.createToken(userDTO, 1); // 1 millisecond lifespan
        Thread.sleep(10); // Ensure expiration
        assertThrows(TokenExpiredException.class, () -> jwtAuthProvider.invalidateToken(expiredToken), "Expired token should throw an exception");
    }

    // token already invalid , invalidate should throw Token expired exception
    @Test
    void testInvalidateToken_InvalidToken_ThrowsTokenInvalidException(){
        String malformedToken = "invalid.token.format";
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.invalidateToken(malformedToken), "Invalid token should throw an exception");
    }

    //  token malformed,blank or null , invalidate should throw Token invalidate exception

    @Test
    void testInvalidateToken_BlankToken_ThrowsTokenInvalidException(){
        String emptyToken = "";
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.invalidateToken(emptyToken), "Empty token should throw InvalidTokenException");

    }

    @Test
    void testInvalidateToken_NullToken_ThrowsTokenInvalidException(){
        String nullToken = null;
        assertThrows(InvalidTokenException.class, () -> jwtAuthProvider.invalidateToken(nullToken), "Null token should throw InvalidTokenException");

    }

}