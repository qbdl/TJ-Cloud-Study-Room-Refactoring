package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Room;
import com.example.backend.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;
import java.util.List;

//@Mapper
public interface RoomMapper extends BaseMapper<Room> {

}
