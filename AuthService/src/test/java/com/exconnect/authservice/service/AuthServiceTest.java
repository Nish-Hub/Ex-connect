package com.exconnect.authservice.service;

import com.exconnect.authservice.authprovider.IAuthProvider;
import com.exconnect.authservice.exceptions.InvalidTokenException;
import com.exconnect.authservice.exceptions.TokenExpiredException;
import com.exconnect.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    IAuthProvider authProvider;

    UserDTO userDTO;

    @BeforeEach
    void setUp() {

        userDTO = UserDTO.builder()
                .userId("1234")
                .userName("Nishant")
                .userRoles(List.of("admin,user"))
                .build();


    }

    // method naming format to be followed : testMethodName_condition_expectedBehavior
    @Test
    void testGenerateToken_withValidUserDTO_returnsToken() {

        when(authProvider.createToken(any(UserDTO.class))).thenReturn("mockToken");
        String token = authService.generateToken(userDTO);
        assertEquals("mockToken",token);
        verify(authProvider).createToken(any(UserDTO.class));

    }

    @Test
    void testValidateToken_withInvalidToken_propagatesExceptionFromAuthProvider() throws InvalidTokenException, TokenExpiredException {
        String token = "invalidToken";
        doThrow(new InvalidTokenException("Invalid Token")).when(authProvider).validateToken(token);
        assertThrows(InvalidTokenException.class,()->authService.validateToken(token));
        verify(authProvider).validateToken(token);
    }

    @Test
    void testValidateToken_withExpiredToken_propagatesExceptionFromAuthProvider() throws InvalidTokenException, TokenExpiredException {

        String token = "expiredToken";
        doThrow(new TokenExpiredException("Expired Token")).when(authProvider).validateToken(token);
        assertThrows(TokenExpiredException.class,()->authService.validateToken(token));
        verify(authProvider).validateToken(token);
    }

    @Test
    void testInvalidateToken_withValidToken_returnsToken() throws InvalidTokenException, TokenExpiredException {
        String token = "validToken";

        when(authProvider.invalidateToken(token)).thenReturn("invalidatedToken");
        String invalidatedToken = this.authService.invalidateToken(token);
        assertEquals("invalidatedToken",invalidatedToken,"Token invalidation failed");
        verify(authProvider).invalidateToken(token);
    }
    @Test
    void testInvalidateToken_withInvalidToken_propagatesExceptionFromAuthProvider() throws InvalidTokenException, TokenExpiredException {
        String token = "invalidToken";
        doThrow(new InvalidTokenException("Invalid Token")).when(authProvider).invalidateToken(token);
        assertThrows(InvalidTokenException.class,()->authService.invalidateToken(token));
        verify(authProvider).invalidateToken(token);
    }

    @Test
    void testInvalidateToken_withExpiredToken_propagatesExceptionFromAuthProvider() throws InvalidTokenException, TokenExpiredException {
        String token = "expiredToken";
        doThrow(new TokenExpiredException("Expired Token")).when(authProvider).invalidateToken(token);
        assertThrows(TokenExpiredException.class,()->authService.invalidateToken(token));
        verify(authProvider).invalidateToken(token);
    }
}