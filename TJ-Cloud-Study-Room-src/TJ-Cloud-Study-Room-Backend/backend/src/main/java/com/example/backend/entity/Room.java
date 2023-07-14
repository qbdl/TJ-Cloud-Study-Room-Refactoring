package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;

@TableName("room_info")//表的名称与类的名称不相等，将表的名称与类的名称对应起来
@Data
public class Room {
    @TableId(value="rid", type = IdType.AUTO)
    private Integer rid;        //room id (main key)
    private String roomName;    //room name(not unique, not main key)
    private Integer fixed;      //is fixed room(cant be deleted)
    private Integer maxNum;     //the capacity of the room
    private Integer preNum;     //the current number of people
    private String roomAnnouncement;//room announcement
    private String roomPassword;//room password
    private Date startTime;//time when built room
    private Integer roomOwner;//房主
    @TableField(exist = false)
    private List<User> users;   //the people list in the room

    //The following are basic functions, but they are not very useful
}
