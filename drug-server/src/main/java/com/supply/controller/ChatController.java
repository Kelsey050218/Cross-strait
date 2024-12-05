package com.supply.controller;

import com.supply.dto.ChatInformationDTO;
import com.supply.result.Result;
import com.supply.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@Tag(name = "聊天信息处理类")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    @Operation(summary = "创建聊天队列接口")
    public Result<Object> createChatQueue(@RequestParam Long id){
        chatService.createChatQueue(id);
        return Result.success();
    }

    @PostMapping("/send")
    @Operation(summary = "聊天信息的接收处理接口")
    public Result<Object> dealChatInformation(@RequestBody @Valid ChatInformationDTO chatInformationDTO){
        chatService.dealChatInformation(chatInformationDTO);
        return Result.success();
    }

    @GetMapping("/queues")
    @Operation(summary = "查询聊天队列接口")
    public Result<Object> getChatQueues(){
        //TODO 做
        return Result.success();
    }
}
