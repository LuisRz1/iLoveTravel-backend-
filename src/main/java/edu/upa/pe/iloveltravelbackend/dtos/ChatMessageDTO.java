package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.services.ChatMessageService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@Data
public class ChatMessageDTO {
    private final ChatMessageService chatMessageService;

    public ChatMessageDTO(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }
}
