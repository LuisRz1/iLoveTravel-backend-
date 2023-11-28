package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ChatMessageService chatMessageService;

    @Autowired
    public UserService(UserRepository userRepository, @Lazy ChatMessageService chatMessageService) {
        this.userRepository = userRepository;
        this.chatMessageService = chatMessageService;
    }

    public List<UserDTO> getAllUserProfiles() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userProfiles = new ArrayList<>();
        for (User user : users) {
            userProfiles.add(new UserDTO(user));
        }
        return userProfiles;
    }
    public List<UserDTO> searchUsers(String firstName, String lastName) {
        List<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);

        if (users.isEmpty()) {
            throw new IllegalStateException("Usuario no encontrado");
        }
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user))
                .collect(Collectors.toList());
        return userDTOs;
    }

    public List<UserDTO> searchUsersByCountry(String country) {
        List<User> users = userRepository.findByNationality(country);

        if (users.isEmpty()) {
            throw new IllegalStateException("Usuarios no encontrados para el país especificado");
        }

        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user))
                .collect(Collectors.toList());
        return userDTOs;
    }



    private boolean isEmptyOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }
    public String addUser(User user){
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (isEmptyOrWhitespace(user.getFirstName()) || isEmptyOrWhitespace(user.getLastName()) || isEmptyOrWhitespace(user.getEmail()) || isEmptyOrWhitespace(user.getNationality()) || isEmptyOrWhitespace(user.getPassword()) || user.getBirthdate() == null) {
            throw new IllegalStateException("Todos los campos son requeridos");
        }
        if(!existingUserByEmail.isEmpty()) {
            throw new IllegalStateException("El correo que ingresaste ya esta en uso");
        }
        if(user.getPassword() != null && user.getPassword().length() > 8){
            throw new IllegalStateException("La contraseña debe tener menos de 8 caracteres");
        }
        user.setRegistrationDate(Instant.now());
        userRepository.save(user);
        return "Usuario registrado correctamente";

    }

    public ResponseEntity<?> getUserProfile(String email) {
        if (isEmptyOrWhitespace(email)) {
            return new ResponseEntity<>("El correo es un campo requerido", HttpStatus.BAD_REQUEST);
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        User user = existingUser.get();

        List<ChatMessage> receivedMessages = chatMessageService.getReceivedMessagesForUser(user);
        int receivedMessageCount = receivedMessages.size();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Sesion iniciada Bienvenido ->", user.getFirstName());

        Map<String, Object> aboutUser = new LinkedHashMap<>();
        aboutUser.put("Nombre Completo", user.getFirstName() + " " + user.getLastName());
        aboutUser.put("Email", user.getEmail());
        aboutUser.put("Nacionalidad", user.getNationality());
        aboutUser.put("Fecha de Cumpleaños", user.getBirthdate().toString());

        Map<String, List<String>> receivedMessagesMap = new LinkedHashMap<>();
        for (ChatMessage message : receivedMessages) {
            String senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();
            receivedMessagesMap.computeIfAbsent(senderName, key -> new ArrayList<>()).add(message.getMessage());
        }

        List<Map<String, Object>> chatsList = receivedMessagesMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> chatData = new LinkedHashMap<>();
                    chatData.put("Enviado por", entry.getKey());
                    chatData.put("Mensaje(s)", entry.getValue());
                    return chatData;
                })
                .collect(Collectors.toList());

        response.put("Acerca de", aboutUser);
        response.put("Cantidad de mensajes recibos", receivedMessageCount);
        response.put("Chats", chatsList);

        return ResponseEntity.ok(response);
    }
}