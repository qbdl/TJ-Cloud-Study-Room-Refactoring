package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@TableName("time_table")
@Data
public class TimeTable {
    /* cid定义为自增 */
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;
    private Integer uid;
    private Integer weekday;
    private String title;
    private String location;
    private Integer start;
    private Integer end;
    private String color;
}
