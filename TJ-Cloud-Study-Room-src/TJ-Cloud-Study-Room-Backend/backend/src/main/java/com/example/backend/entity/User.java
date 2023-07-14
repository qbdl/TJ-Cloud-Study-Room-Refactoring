package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@TableName("user_info")
@Data
public class User {
    /* userid定义为自增 */
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;
    private String userName;
    private String pwd;
    private String sex;
    private String major;
    private String grade;
    private String educationLevel;
    private Long phoneNumber;
    private String mailAddress;
    private Integer onlineState;
    private Long loginTime;
    private Integer roomId;
    private Date roomEnterTime;
    private String picture;
}
