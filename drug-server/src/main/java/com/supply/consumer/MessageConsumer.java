package com.supply.consumer;

import com.supply.constant.FormattingConstant;
import com.supply.websocket.ChatEndPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageConsumer {

    private final ChatEndPoint chatEndPoint;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue1"),
            exchange = @Exchange(name = "chat.direct1"),
            key = {"chatDirect1"}
    ))
    public void listenDirect1Queue(String message) {
        log.info("接收到chat.direct1的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue2"),
            exchange = @Exchange(name = "chat.direct2"),
            key = {"chatDirect2"}
    ))
    public void listenDirect2Queue(String message) {
        log.info("接收到chat.direct2的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue3"),
            exchange = @Exchange(name = "chat.direct3"),
            key = {"chatDirect3"}
    ))
    public void listenDirect3Queue(String message) {
        log.info("接收到chat.direct3的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue4"),
            exchange = @Exchange(name = "chat.direct4"),
            key = {"chatDirect4"}
    ))
    public void listenDirect4Queue(String message) {
        log.info("接收到chat.direct4的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue5"),
            exchange = @Exchange(name = "chat.direct5"),
            key = {"chatDirect5"}
    ))
    public void listenDirect5Queue(String message) {
        log.info("接收到chat.direct5的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue6"),
            exchange = @Exchange(name = "chat.direct6"),
            key = {"chatDirect6"}
    ))
    public void listenDirect6Queue(String message) {
        log.info("接收到chat.direct6的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue7"),
            exchange = @Exchange(name = "chat.direct7"),
            key = {"chatDirect7"}
    ))
    public void listenDirect7Queue(String message) {
        log.info("接收到chat.direct7的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue8"),
            exchange = @Exchange(name = "chat.direct8"),
            key = {"chatDirect8"}
    ))
    public void listenDirect8Queue(String message) {
        log.info("接收到chat.direct8的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue9"),
            exchange = @Exchange(name = "chat.direct9"),
            key = {"chatDirect9"}
    ))
    public void listenDirect9Queue(String message) {
        log.info("接收到chat.direct9的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "chat.queue10"),
            exchange = @Exchange(name = "chat.direct10"),
            key = {"chatDirect10"}
    ))
    public void listenDirect10Queue(String message) {
        log.info("接收到chat.direct10的消息：{}", message + "," + LocalDateTime.now().format(FormattingConstant.LOCAL_DATE_TIME_FORMATTER));
        chatEndPoint.parseSendAndStore(message);
    }

}
