package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("new_friend_message")//表的名称与类的名称不相等，将表的名称与类的名称对应起来
@Data
public class NewFriendMessage {
    @TableId(value="id", type = IdType.AUTO)
    private Integer id;          //meseage id (main key)
    private Integer senderId;    //sender id
    private Integer receiverId;  //receiver id
    private Integer state;       //processed(1) or not_processed(0)
    private Date sendTime;
}