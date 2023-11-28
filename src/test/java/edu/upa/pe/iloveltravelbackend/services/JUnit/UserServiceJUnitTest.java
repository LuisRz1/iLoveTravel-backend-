package edu.upa.pe.iloveltravelbackend.services.JUnit;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserServiceJUnitTest {
    private UserRepository userRepository;
    private ChatMessageService chatMessageService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        chatMessageService = mock(ChatMessageService.class);
        userService = new UserService(userRepository, chatMessageService);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setNationality("USA");
        user.setPassword("password");
        user.setBirthdate(LocalDate.of(1990, 1, 1));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(new ArrayList<>());

        String result = userService.addUser(user);

        assertEquals("Usuario registrado correctamente", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAddUserWithDuplicateEmail() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Collections.singletonList(user));

        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAddUserWithLongPassword() {
        User user = new User();
        user.setPassword("passwordtoolong");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(new ArrayList<>());

        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAddUserWithMissingRequiredFields() {
        User user = new User(); // Campos requeridos faltantes

        assertThrows(IllegalStateException.class, () -> userService.addUser(user));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.singletonList(user));
        when(chatMessageService.getReceivedMessagesForUser(user)).thenReturn(new ArrayList<>());

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
