package edu.upa.pe.iloveltravelbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.upa.pe.iloveltravelbackend.models.ChatMessage;
import edu.upa.pe.iloveltravelbackend.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage>findAllByReceiverOrderByDateSentAsc(User receiver);
    List<ChatMessage>findAllBySenderAndReceiverOrderByDateSentAsc(User sender, User receiver);
}
