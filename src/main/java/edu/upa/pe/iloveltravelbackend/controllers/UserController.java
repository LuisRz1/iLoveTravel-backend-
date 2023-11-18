package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ChatMessageService chatMessageService; // Agregar la inyección de ChatMessageService

    public UserController(UserService userService, ChatMessageService chatMessageService) {
        this.userService = userService;
        this.chatMessageService = chatMessageService; // Inyectar el servicio de ChatMessageService
    }
    @GetMapping("/profiles")
    public List<UserDTO> getAllUserProfiles() {
        return userService.getAllUserProfiles();
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody Map<String, String> searchRequest) {
        if (searchRequest.containsKey("firstName") && searchRequest.containsKey("lastName")) {
            // Búsqueda por nombre
            String firstName = searchRequest.get("firstName");
            String lastName = searchRequest.get("lastName");
            try {
                List<UserDTO> users = userService.searchUsers(firstName, lastName);
                return ResponseEntity.ok(users);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else if (searchRequest.containsKey("nationality")) {
            // Búsqueda por país
            String nationality = searchRequest.get("nationality");
            try {
                List<UserDTO> users = userService.searchUsersByCountry(nationality);
                return ResponseEntity.ok(users);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros de búsqueda incorrectos");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user){
        try{
            String newUser = userService.addUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalStateException sms){
            return new ResponseEntity<>(sms.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            return userService.login(email, password);
        } catch (IllegalStateException sms) {
            return new ResponseEntity<>(sms.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
