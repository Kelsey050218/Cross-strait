package com.supply.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supply.config.WebSocketJwtConfig;
import com.supply.constant.MessageConstant;
import com.supply.context.WebSocketContext;
import com.supply.entity.ChatInformation;
import com.supply.entity.ChatQueue;
import com.supply.enumeration.WebSocketSendStatus;
import com.supply.exception.WebSocketSendException;
import com.supply.mapper.ChatMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/chat", configurator = WebSocketJwtConfig.class)
@Component
@Slf4j
@RequiredArgsConstructor
public class ChatEndPoint {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    private final ChatMapper chatMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;


    @OnOpen
    public void onOpen(Session session) {
        onlineUsers.put(WebSocketContext.getCurrentId().toString(), session);
        //检查消息的发送情况，发送未发送的信息
        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        List<Object> messages = listOperations.range("unsentMessages:" + WebSocketContext.getCurrentId(), 0, -1);
        if (messages != null) {
            for (Object message : messages) {
                Random random = new Random();
                int i = random.nextInt(10);
                String exchangeName = "chat.direct" + i;
                String routingKey = "chatDirect" + i;
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend(exchangeName, routingKey, message, correlationData);
            }
        }
        //再将每个聊天队列的100条信息进行缓存
        List<ChatQueue> queues = chatMapper.getAllQueueByUserId(WebSocketContext.getCurrentId());
        for (ChatQueue queue : queues) {
            List<ChatInformation> chatInformationByQueueId = chatMapper.getSomeChatInformationByQueueId(queue.getId());
            String information = JSON.toJSONString(chatInformationByQueueId);
            redisTemplate.opsForValue().set(queue.getId(), information);
        }
    }


    @OnClose
    public void OnClose() {
        onlineUsers.remove(WebSocketContext.getCurrentId().toString());
        //对所有没有聊天的队列进行删除
        List<ChatQueue> chatQueues = chatMapper.getAllQueueByUserId(WebSocketContext.getCurrentId());
        for (ChatQueue chatQueue : chatQueues) {
            List<ChatInformation> chatInformation = chatMapper.getChatInformationByQueueId(chatQueue.getId());
            if (chatInformation == null || chatInformation.isEmpty()) {
                chatMapper.deleteQueueById(chatQueue.getId());
            }
        }
        //移除线程
        WebSocketContext.removeCurrentId();
    }


    private WebSocketSendStatus sendMessage(String id, String message) {
        Session session = onlineUsers.get(id);
        if (session != null && session.isOpen()) {
            log.info("开始发送消息给：{}，消息内容为：{}", id, message);
            synchronized (session) {
                try {
                    session.getBasicRemote().sendText(message);
                    log.info("发送消息成功");
                    return WebSocketSendStatus.SUCCESS;
                } catch (IOException e) {
                    log.error("发送给：{}的消息失败：{}", id, message);
                    throw new WebSocketSendException(MessageConstant.WEBSOCKET_SEND_FAILED);
                }
            }
        }
        return WebSocketSendStatus.SESSION_NOT_FOUND;
    }


    private WebSocketSendStatus sendImage(String id, String image) {
        Session session = onlineUsers.get(id);
        if (session != null && session.isOpen()) {
            log.info("开始发送音视频文件给：{}，消息内容为：{}", id, image);
            synchronized (session) {
                try {
                    session.getBasicRemote().sendText(image);
                    return WebSocketSendStatus.SUCCESS;
                } catch (IOException e) {
                    log.error("发送给：{}的音视频文件失败：{}", id, image);
                    throw new WebSocketSendException(MessageConstant.WEBSOCKET_SEND_FAILED);
                }
            }
        }
        return WebSocketSendStatus.SESSION_NOT_FOUND;
    }

    public void parseSendAndStore(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String receiveUserId = jsonObject.getString("receiveUserId");
        String information = jsonObject.getString("information");
        String image = jsonObject.getString("image");
        if (information != null) {
            if (sendMessage(receiveUserId, information) == WebSocketSendStatus.SUCCESS) {
                //存储聊天数据
                ChatInformation chatInformation = JSON.parseObject(message, ChatInformation.class);
                chatMapper.storeChatInformation(chatInformation);
                //删除未发送信息
                ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
                listOperations.remove("unsentMessages:" + receiveUserId, 1, message);
            }
        } else {
            if (sendImage(receiveUserId, image) == WebSocketSendStatus.SUCCESS) {
                //存储聊天数据
                ChatInformation chatInformation = JSON.parseObject(message, ChatInformation.class);
                chatMapper.storeChatInformation(chatInformation);
                //删除未发送信息
                ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
                listOperations.remove("unsentMessages:" + receiveUserId, 1, message);
            }
        }
    }
}
