package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {
    private final ChatMessageDTO chatMessageDTO;
    @Autowired
    public ChatController(ChatMessageDTO chatMessageDTO) {
        this.chatMessageDTO = chatMessageDTO;
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> messageData) {
        return chatMessageDTO.sendMessage(messageData);
    }

    @GetMapping("/getMessages")
    public ResponseEntity<?> getMessages(
            @RequestParam("senderFirstName") String senderFirstName,
            @RequestParam("senderLastName") String senderLastName,
            @RequestParam("receiverFirstName") String receiverFirstName,
            @RequestParam("receiverLastName") String receiverLastName) {
        return chatMessageDTO.getMessages(senderFirstName, senderLastName, receiverFirstName, receiverLastName);
    }
}
