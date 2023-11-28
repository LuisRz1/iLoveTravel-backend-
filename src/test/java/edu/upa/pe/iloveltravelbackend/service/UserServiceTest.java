package edu.upa.pe.iloveltravelbackend.service;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUserProfiles() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "John", "Doe", "john@example.com", "password", "USA", LocalDate.now(), Instant.now()));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> userProfiles = userService.getAllUserProfiles();

        // Assert
        assertEquals(1, userProfiles.size());
        assertEquals("John", userProfiles.get(0).getFirstName());
        assertEquals("Doe", userProfiles.get(0).getLastName());
    }

    @Test
    void searchUsers() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        List<User> users = new ArrayList<>();
        users.add(new User(1L, firstName, lastName, "john@example.com", "password", "USA", LocalDate.now(), Instant.now()));
        when(userRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(users);

        // Act
        List<UserDTO> userDTOs = userService.searchUsers(firstName, lastName);

        // Assert
        assertEquals(1, userDTOs.size());
        assertEquals(firstName, userDTOs.get(0).getFirstName());
        assertEquals(lastName, userDTOs.get(0).getLastName());
    }

    @Test
    void searchUsersByCountry() {
        // Arrange
        String country = "USA";
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "John", "Doe", "john@example.com", "password", country, LocalDate.now(), Instant.now()));
        when(userRepository.findByNationality(country)).thenReturn(users);

        // Act
        List<UserDTO> userDTOs = userService.searchUsersByCountry(country);

        // Assert
        assertEquals(1, userDTOs.size());
        assertEquals("John", userDTOs.get(0).getFirstName());
        assertEquals("Doe", userDTOs.get(0).getLastName());
        assertEquals(country, userDTOs.get(0).getNationality());
    }

    @Test
    void addUser() {
        // Arrange
        User user = new User(null, "John", "Doe", "john@example.com", "password", "USA", LocalDate.now(), Instant.now());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        String result = userService.addUser(user);

        // Assert
        assertEquals("Usuario registrado correctamente", result);
    }

    @Test
    void addUserWithEmailInUse() {
        // Arrange
        User user = new User(null, "John", "Doe", "john@example.com", "password", "USA", LocalDate.now(), Instant.now());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> userService.addUser(user));
    }

    @Test
    void getUserProfile() {
        // Arrange
        String email = "john@example.com";
        User user = new User(null, "John", "Doe", email, "password", "USA", LocalDate.now(), Instant.now());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> responseEntity = userService.getUserProfile(email);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getUserProfileWithEmailMissing() {
        // Arrange
        String email = "";

        // Act
        ResponseEntity<?> responseEntity = userService.getUserProfile(email);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
