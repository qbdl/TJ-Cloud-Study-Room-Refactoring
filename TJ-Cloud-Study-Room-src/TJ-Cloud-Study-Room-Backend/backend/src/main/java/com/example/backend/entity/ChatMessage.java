package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("chat_message")//表的名称与类的名称不相等，将表的名称与类的名称对应起来
@Data
public class ChatMessage {
    @TableId(value="id", type = IdType.AUTO)
    private Integer id;
    private String fromUser;
    private String toUser;
    private String text;
    private Date sendDate;
}
