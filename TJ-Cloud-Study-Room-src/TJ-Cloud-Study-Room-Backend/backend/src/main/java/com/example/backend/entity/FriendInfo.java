package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("friend_info")//表的名称与类的名称不相等，将表的名称与类的名称对应起来
@Data
public class FriendInfo {
    @TableId(value="id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer friendId;


}
