package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ChatMessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
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

    public User getUserByFirstNameAndLastName(String firstName, String lastName) {
        Optional<User> userOptional = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return userOptional.orElse(null); // or throw an exception if needed
    }
}
