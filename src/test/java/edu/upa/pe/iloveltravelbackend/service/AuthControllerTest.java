package edu.upa.pe.iloveltravelbackend.service;

import edu.upa.pe.iloveltravelbackend.controllers.UserController;
import edu.upa.pe.iloveltravelbackend.mappers.LoginRequest;
import edu.upa.pe.iloveltravelbackend.mappers.LoginResponse;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import edu.upa.pe.iloveltravelbackend.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSuccess() throws Exception {
        // Arrange
        String email = "john@example.com";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(email, password);
        User user = new User(1L, "John", "Doe", email, password, "USA", LocalDate.now(), Instant.now());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtTokenUtil.generateToken(user)).thenReturn("fakeToken");

        // Act
        LoginResponse response = userController.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    void loginFailureInvalidCredentials() throws Exception {
        // Arrange
        String email = "john@example.com";
        String password = "incorrectPassword";
        LoginRequest loginRequest = new LoginRequest(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> userController.login(loginRequest));
    }

    @Test
    void loginFailureAuthenticationException() throws Exception {
        // Arrange
        String email = "john@example.com";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(email, password);
        User user = new User(1L, "John", "Doe", email, password, "USA", LocalDate.now(), Instant.now());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {});

        // Act and Assert
        assertThrows(Exception.class, () -> userController.login(loginRequest));
    }
}