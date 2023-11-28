package edu.upa.pe.iloveltravelbackend.services.mockito;

import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
public class UserServiceMockitoTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser(){
        // Arrange (GIVEN)
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setNationality("USA");
        user.setPassword("password");
        user.setBirthdate(LocalDate.of(1990, 1, 1));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(new ArrayList<>());

        // Act (WHEN)
        String result = userService.addUser(user);

        // Assert (THEN)
        assertEquals("Usuario registrado correctamente", result);
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testAddUserWithDuplicateEmail(){
        // Arrange (GIVEN)
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Collections.singletonList(user));

        // Act (WHEN)
        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        // Assert (THEN)
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAddUserWithLongPassword(){
        // Arrange (GIVEN)
        User user = new User();
        user.setPassword("passwordtoolong");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(new ArrayList<>());

        // Act (WHEN)
        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        // Assert (THEN)
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAddUserWithMissingRequiredFields(){
        // Arrange (GIVEN)
        User user = new User();
        // Act (WHEN)
        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        // Assert (THEN)
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.singletonList(user));
        Mockito.when(chatMessageService.getReceivedMessagesForUser(user)).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = userService.login("test@example.com", "password");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginMissingEmailOrPassword() {
        ResponseEntity<?> response = userService.login("", "password");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLoginIncorrectEmailOrPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = userService.login("test@example.com", "wrongpassword");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
