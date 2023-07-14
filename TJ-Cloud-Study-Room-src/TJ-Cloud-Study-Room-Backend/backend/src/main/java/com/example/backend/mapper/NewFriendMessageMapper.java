package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.NewFriendMessage;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NewFriendMessageMapper extends BaseMapper<NewFriendMessage> {
    @Select("select * from new_friend_message where new_friend_message.receiver_id = #{receiverId} " +
            "and new_friend_message.state = 0")
    List<NewFriendMessage> getUnprocessedMessage(int receiverId);

    @Select("select * from new_friend_message where new_friend_message.receiver_id = #{receiverId} " +
            "and new_friend_message.state != 0")
    List<NewFriendMessage> getProcessedMessage(int receiverId);
    @Select("select * from new_friend_message where new_friend_message.receiver_id = #{uid} ")
    List<NewFriendMessage> getmyMessage(int uid);

    @Select("select * from new_friend_message where new_friend_message.sender_id = #{uid} ")
    List<NewFriendMessage> getmyRequest(int uid);
}
