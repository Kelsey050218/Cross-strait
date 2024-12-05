package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.supply.context.BaseContext;
import com.supply.dto.ChatInformationDTO;
import com.supply.entity.ChatInformation;
import com.supply.entity.ChatQueue;
import com.supply.mapper.ChatMapper;
import com.supply.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 创建聊天队列
     *
     * @param id 对方聊天人id
     */
    public void createChatQueue(Long id) {
        chatMapper.createChatQueue(BaseContext.getCurrentId(),id,LocalDateTime.now());
    }

    /**
     * 聊天信息的接收处理
     *
     * @param chatInformationDTO 聊天信息
     */
    public void dealChatInformation(ChatInformationDTO chatInformationDTO) {
        //先将信息放入redis
        ChatQueue chatQueue = chatMapper.getQueue(chatInformationDTO.getId());
        Long receiveUserId;
        if(Objects.equals(chatQueue.getUserId1(), BaseContext.getCurrentId())) {
            receiveUserId = chatQueue.getUserId2();
        }else{
            receiveUserId = chatQueue.getUserId1();
        }
        log.info("收信人id：{}", receiveUserId);
        ChatInformation chatInformation = ChatInformation.builder()
                .queueId(chatQueue.getId())
                .information(chatInformationDTO.getInformation())
                .receiveUserId(receiveUserId)
                .sendUserId(BaseContext.getCurrentId())
                .image(chatInformationDTO.getImage())
                .sendTime(LocalDateTime.now())
                .build();
        String informationJson = JSON.toJSONString(chatInformation);
        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("unsentMessages:" + receiveUserId, informationJson);
        //再将信息发送到mq
        Random random = new Random();
        int i = random.nextInt(10);
        String exchangeName = "chat.direct" + i;
        String routingKey= "chatDirect" + i;
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, informationJson, correlationData);
    }
}
