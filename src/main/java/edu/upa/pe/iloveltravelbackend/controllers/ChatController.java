package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> messageData) {
        ChatMessage chatMessage = chatMessageService.sendMessage(messageData);
        if (chatMessage != null) {
            return ResponseEntity.ok("Mensaje enviado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el mensaje");
        }
    }
}
