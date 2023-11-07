package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ChatMessageRepository;

import java.time.Instant;
import java.util.*;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository, UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesBySenderAndReceiver(User sender, User receiver) {
        return chatMessageRepository.findAllBySenderAndReceiverOrderByDateSentAsc(sender, receiver);
    }

    public List<ChatMessage> getReceivedMessagesForUser(User receiver) {
        return chatMessageRepository.findAllByReceiverOrderByDateSentAsc(receiver);
    }

    public int getReceivedMessagesCount(User user) {
        List<ChatMessage> receivedMessages = chatMessageRepository.findAllByReceiverOrderByDateSentAsc(user);
        return receivedMessages.size();
    }


    public ChatMessage sendMessage(Map<String, String> messageData) {
        try {
            String senderFirstName = messageData.get("senderFirstName");
            String senderLastName = messageData.get("senderLastName");
            String receiverFirstName = messageData.get("receiverFirstName");
            String receiverLastName = messageData.get("receiverLastName");

            List<User> senders = userRepository.findByFirstNameAndLastName(senderFirstName, senderLastName);
            List<User> receivers = userRepository.findByFirstNameAndLastName(receiverFirstName, receiverLastName);

            if (senders.isEmpty() || receivers.isEmpty()) {
                throw new IllegalStateException("Usuario remitente o destinatario no encontrado");
            }

            User sender = senders.get(0);
            User receiver = receivers.get(0);

            String messageText = messageData.get("message");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setReceiver(receiver);
            chatMessage.setMessage(messageText);
            chatMessage.setDateSent(Instant.now());

            // Guardar el mensaje en la base de datos u en la lógica de tu aplicación
            chatMessage = saveChatMessage(chatMessage);

            // Devolver el mensaje guardado
            return chatMessage;
        } catch (Exception e) {
            // Manejar errores y excepciones aquí
            // Puedes lanzar una excepción específica o manejar el error de otra manera
            throw new IllegalStateException("Error al enviar el mensaje: " + e.getMessage());
        }
    }
}
