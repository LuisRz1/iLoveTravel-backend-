package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@Data
public class ChatMessageDTO {
    private final ChatMessageService chatMessageService;

    public ChatMessageDTO(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    public ResponseEntity<String> sendMessage(Map<String, String> messageData) {
        try {
            String senderFirstName = messageData.get("senderFirstName");
            String senderLastName = messageData.get("senderLastName");
            User sender = chatMessageService.getUserByFirstNameAndLastName(senderFirstName, senderLastName);

            if (sender == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario remitente no encontrado");
            }

            String receiverFirstName = messageData.get("receiverFirstName");
            String receiverLastName = messageData.get("receiverLastName");
            User receiver = chatMessageService.getUserByFirstNameAndLastName(receiverFirstName, receiverLastName);

            if (receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario destinatario no encontrado");
            }

            String messageText = messageData.get("message");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setReceiver(receiver);
            chatMessage.setMessage(messageText);
            chatMessage.setDateSent(Instant.now());
            chatMessageService.saveChatMessage(chatMessage);

            return ResponseEntity.ok("Mensaje enviado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getMessages(String senderFirstName, String senderLastName, String receiverFirstName, String receiverLastName) {
        try {
            User sender = chatMessageService.getUserByFirstNameAndLastName(senderFirstName, senderLastName);
            User receiver = chatMessageService.getUserByFirstNameAndLastName(receiverFirstName, receiverLastName);

            if (sender == null || receiver == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuarios no encontrados");
            }

            List<ChatMessage> messages = chatMessageService.getMessagesBySenderAndReceiver(sender, receiver);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("Bienvenido a su chat con ", receiver.getFirstName() + " " + receiver.getLastName());

            List<String> messageList = new ArrayList<>();

            for (ChatMessage chatMessage : messages) {
                messageList.add(chatMessage.getMessage());
            }

            response.put("messages", messageList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener mensajes: " + e.getMessage());
        }
    }
}
