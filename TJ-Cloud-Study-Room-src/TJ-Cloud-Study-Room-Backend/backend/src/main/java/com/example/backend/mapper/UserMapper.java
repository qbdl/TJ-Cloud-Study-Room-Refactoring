package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.FriendInfo;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/*继承*/
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user_info where user_info.room_id=#{rid}")
    List<User> selectUidByRid(int rid);

    @Select("SELECT COUNT(*) FROM user_info where user_info.online_state = 1")
    int getUserNum();

    @Select("select * from user_info where user_info.user_name = #{uname} ")
    List<User> getUserListByUname(String uname);

    @Select("select * from user_info where user_info.user_name = #{uname} ")
    User getUserByUname(String uname);
}
