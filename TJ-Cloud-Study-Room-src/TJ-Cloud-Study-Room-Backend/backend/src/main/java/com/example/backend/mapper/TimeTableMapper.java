package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.NewFriendMessage;
import com.example.backend.entity.TimeTable;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/*继承*/
public interface TimeTableMapper extends BaseMapper<TimeTable> {
    @Select("select * from time_table where time_table.uid = #{uid} ")
    List<TimeTable> getUserTimeTable(int uid);

    @Select("select * from user_info")
    List<User> getUser();
}
