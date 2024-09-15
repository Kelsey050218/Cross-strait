package com.supply.service;

import com.supply.dto.ChatInformationDTO;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    void createChatQueue(Long id);

    void dealChatInformation(ChatInformationDTO chatInformationDTO);
}
