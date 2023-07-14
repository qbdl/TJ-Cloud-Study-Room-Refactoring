package com.example.backend.controller;

import com.example.backend.common.Result;
import com.example.backend.entity.ChatMessage;
import com.example.backend.mapper.ChatMessageMapper;
import com.example.backend.mapper.FriendInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping()
public class ChatMessageController {
    @Resource
    ChatMessageMapper chatMessageMapper;

    //添加message
    @PostMapping("/sendMessage")
    public Result<?> sendMessage(@RequestBody ChatMessage chatMessage){
        chatMessage.setSendDate(new Date());
        chatMessageMapper.insert(chatMessage);
        return Result.success();
    }

    //获取用户的消息
    @GetMapping("/getMessage")
    public Result<?> getMessage(@RequestParam String uname, @RequestParam String fname){
        List<ChatMessage> chatMessageList = chatMessageMapper.getMessageList(uname, fname);
        return Result.success(chatMessageList);
    }
}
