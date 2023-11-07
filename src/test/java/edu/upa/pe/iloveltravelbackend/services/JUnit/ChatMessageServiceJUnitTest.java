package edu.upa.pe.iloveltravelbackend.services.JUnit;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ChatMessageRepository;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class ChatMessageServiceJUnitTest {
    private ChatMessageService chatMessageService;
    private ChatMessageRepository chatMessageRepository;
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        chatMessageRepository = mock(ChatMessageRepository.class);
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);
        chatMessageService = new ChatMessageService(chatMessageRepository, userRepository, userService);
    }

    @Test
    void saveChatMessage() {
        // Arrange
        ChatMessage chatMessage = new ChatMessage();

        // Act
        chatMessageService.saveChatMessage(chatMessage);

        // Assert
        verify(chatMessageRepository).save(chatMessage);
    }

    @Test
    void getMessagesBySenderAndReceiver() {
        // Arrange
        User sender = new User();
        User receiver = new User();
        when(chatMessageRepository.findAllBySenderAndReceiverOrderByDateSentAsc(sender, receiver)).thenReturn(new ArrayList<>());

        // Act
        List<ChatMessage> messages = chatMessageService.getMessagesBySenderAndReceiver(sender, receiver);

        // Assert
        verify(chatMessageRepository).findAllBySenderAndReceiverOrderByDateSentAsc(sender, receiver);
        assert(messages.isEmpty());
    }

    @Test
    void getReceivedMessagesForUser() {
        // Arrange
        User receiver = new User();
        when(chatMessageRepository.findAllByReceiverOrderByDateSentAsc(receiver)).thenReturn(new ArrayList<>());

        // Act
        List<ChatMessage> receivedMessages = chatMessageService.getReceivedMessagesForUser(receiver);

        // Assert
        verify(chatMessageRepository).findAllByReceiverOrderByDateSentAsc(receiver);
        assert(receivedMessages.isEmpty());
    }

    @Test
    void getReceivedMessagesCount() {
        // Arrange
        User user = new User();
        List<ChatMessage> receivedMessages = new ArrayList<>();
        when(chatMessageRepository.findAllByReceiverOrderByDateSentAsc(user)).thenReturn(receivedMessages);

        // Act
        int receivedMessagesCount = chatMessageService.getReceivedMessagesCount(user);

        // Assert
        verify(chatMessageRepository).findAllByReceiverOrderByDateSentAsc(user);
        assert(receivedMessagesCount == receivedMessages.size());
    }

    @Test
    void sendMessage() {
        // Arrange
        Map<String, String> messageData = Map.of(
                "senderFirstName", "John",
                "senderLastName", "Doe",
                "receiverFirstName", "Jane",
                "receiverLastName", "Doe",
                "message", "Hello, Jane!"
        );
        User sender = new User();
        User receiver = new User();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setMessage("Hello, Jane!");
        chatMessage.setDateSent(Instant.now());

        when(userRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(sender));
        when(userRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(List.of(receiver));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        // Act
        ChatMessage savedMessage = chatMessageService.sendMessage(messageData);

        // Assert
        verify(userRepository, times(2)).findByFirstNameAndLastName(anyString(), anyString());
        verify(chatMessageRepository).save(any(ChatMessage.class));
        assert(savedMessage.getSender() == sender);
        assert(savedMessage.getReceiver() == receiver);
        assert(savedMessage.getMessage().equals("Hello, Jane!"));
    }
}