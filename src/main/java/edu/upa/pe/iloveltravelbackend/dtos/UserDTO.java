package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthdate;
    private String email;

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.nationality = user.getNationality();
        this.birthdate = user.getBirthdate();
        this.email = user.getEmail();
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getNationality() {
        return nationality;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public String getEmail() {
        return email;
    }

    // Nuevo método para manejar el inicio de sesión
    public Map<String, Object> login(User user, List<ChatMessage> receivedMessages, int receivedMessageCount) {
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
        response.put("Cantidad de mensajes recibidos", receivedMessageCount);
        response.put("Chats", chatsList);

        return response;
    }

}