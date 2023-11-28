package edu.upa.pe.iloveltravelbackend.services.mockito;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ChatMessageRepository;
import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import edu.upa.pe.iloveltravelbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.time.Instant;
public class ChatMessageSeriveMockitoTest {
    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize mockito annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveChatMessage() {
        // Arrange
        ChatMessage chatMessage = new ChatMessage();

        // Act
        chatMessageService.saveChatMessage(chatMessage);

        // Assert
        Mockito.verify(chatMessageRepository).save(chatMessage);
    }

    @Test
    void getMessagesBySenderAndReceiver() {
        // Arrange
        User sender = new User();
        User receiver = new User();
        Mockito.when(chatMessageRepository.findAllBySenderAndReceiverOrderByDateSentAsc(sender, receiver)).thenReturn(new ArrayList<>());

        // Act
        List<ChatMessage> messages = chatMessageService.getMessagesBySenderAndReceiver(sender, receiver);

        // Assert
        Mockito.verify(chatMessageRepository).findAllBySenderAndReceiverOrderByDateSentAsc(sender, receiver);
        assert(messages.isEmpty());
    }

    @Test
    void getReceivedMessagesForUser() {
        // Arrange
        User receiver = new User();
        Mockito.when(chatMessageRepository.findAllByReceiverOrderByDateSentAsc(receiver)).thenReturn(new ArrayList<>());

        // Act
        List<ChatMessage> receivedMessages = chatMessageService.getReceivedMessagesForUser(receiver);

        // Assert
        Mockito.verify(chatMessageRepository).findAllByReceiverOrderByDateSentAsc(receiver);
        assert(receivedMessages.isEmpty());
    }

    @Test
    void getReceivedMessagesCount() {
        // Arrange
        User user = new User();
        List<ChatMessage> receivedMessages = new ArrayList<>();
        Mockito.when(chatMessageRepository.findAllByReceiverOrderByDateSentAsc(user)).thenReturn(receivedMessages);

        // Act
        int receivedMessagesCount = chatMessageService.getReceivedMessagesCount(user);

        // Assert
        Mockito.verify(chatMessageRepository).findAllByReceiverOrderByDateSentAsc(user);
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

        Mockito.when(userRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(sender));
        Mockito.when(userRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(List.of(receiver));
        Mockito.when(chatMessageRepository.save(Mockito.any(ChatMessage.class))).thenReturn(chatMessage);

        // Act
        ChatMessage savedMessage = chatMessageService.sendMessage(messageData);

        // Assert
        Mockito.verify(userRepository, Mockito.times(2)).findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(chatMessageRepository).save(Mockito.any(ChatMessage.class));
        assert(savedMessage.getSender() == sender);
        assert(savedMessage.getReceiver() == receiver);
        assert(savedMessage.getMessage().equals("Hello, Jane!"));
    }
}