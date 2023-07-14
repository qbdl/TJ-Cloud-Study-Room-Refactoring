package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.ChatMessage;
import com.example.backend.entity.FriendInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ChatMessageMapper extends BaseMapper<ChatMessage>{

    @Select("select * from chat_message where " +
            "(chat_message.from_user = #{uname} AND chat_message.to_user = #{fname}) " +
            "OR (chat_message.from_user = #{fname} AND chat_message.to_user = #{uname}) ")

    List<ChatMessage> getMessageList(String uname, String fname);
}
