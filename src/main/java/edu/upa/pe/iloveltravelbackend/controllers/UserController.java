package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import edu.upa.pe.iloveltravelbackend.mappers.LoginRequest;
import edu.upa.pe.iloveltravelbackend.mappers.LoginResponse;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.util.EncryptionUtil;
import edu.upa.pe.iloveltravelbackend.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;
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
            String firstName = searchRequest.get("firstName");
            String lastName = searchRequest.get("lastName");
            try {
                List<UserDTO> users = userService.searchUsers(firstName, lastName);
                return ResponseEntity.ok(users);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        } else if (searchRequest.containsKey("nationality")) {
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
    LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception{
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if(user.isPresent()){
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                return new LoginResponse(EncryptionUtil.encrypt(jwtTokenUtil.generateToken(user.get())));
            }catch (AuthenticationException e){
                //pass to the throw.
            }
        }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo y/o contraseña incorrecta");
        }
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String email) {
        return userService.getUserProfile(email);
    }
}
