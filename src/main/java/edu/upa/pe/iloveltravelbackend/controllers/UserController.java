package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ChatMessageService chatMessageService; // Agregar la inyección de ChatMessageService

    public UserController(UserService userService, ChatMessageService chatMessageService) {
        this.userService = userService;
        this.chatMessageService = chatMessageService; // Inyectar el servicio
    }
    @GetMapping
    private List<UserDTO> getAllUsersProfiles(){
        return userService.getAllUserProfiles();
    }
    @PostMapping
    public String addUser(@RequestBody User user){
        user.setRegistrationDate(Instant.now());
        userService.addUser(user);
        return "Registrado correctamente";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            User user = userService.verifyAccount(loginRequest.get("email"), loginRequest.get("password"));

            // Obtén la cantidad total de mensajes recibidos
            int receivedMessageCount = chatMessageService.getReceivedMessagesCount(user);

            // Obtén la lista de mensajes recibidos
            List<ChatMessage> receivedMessages = chatMessageService.getReceivedMessagesForUser(user);

            // Agrupa los mensajes por el nombre del remitente
            Map<String, List<String>> receivedMessagesMap = new LinkedHashMap<>();
            for (ChatMessage message : receivedMessages) {
                String senderName = message.getSender().getFirstName() + " " + message.getSender().getLastName();
                receivedMessagesMap.computeIfAbsent(senderName, key -> new ArrayList<>()).add(message.getMessage());
            }

            // Crea un objeto JSON para la respuesta
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("Sesion iniciada Bienvenido ->", user.getFirstName());

            Map<String, Object> aboutUser = new LinkedHashMap<>();
            aboutUser.put("Nombre Completo", user.getFirstName() + " " + user.getLastName());
            aboutUser.put("Email", user.getEmail());
            aboutUser.put("Nacionalidad", user.getNationality());
            aboutUser.put("Fecha de Cumpleaños", user.getBirthdate().toString());

            List<Map<String, Object>> chatsList = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : receivedMessagesMap.entrySet()) {
                Map<String, Object> chatData = new LinkedHashMap<>();
                chatData.put("Enviado por", entry.getKey());
                chatData.put("Mensaje(s)", entry.getValue());
                chatsList.add(chatData);
            }
            response.put("Acerca de", aboutUser);
            response.put("Cantidad de mensajes recibidos", receivedMessageCount);
            response.put("Chats", chatsList);

            return ResponseEntity.ok(response);
        } catch (IllegalStateException sms) {
            return new ResponseEntity<>(sms.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
