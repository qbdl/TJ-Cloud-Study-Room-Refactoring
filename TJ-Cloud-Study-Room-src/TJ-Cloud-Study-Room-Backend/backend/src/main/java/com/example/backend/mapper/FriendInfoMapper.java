package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.FriendInfo;
import com.example.backend.entity.NewFriendMessage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FriendInfoMapper extends BaseMapper<FriendInfo> {
    @Select("select * from friend_info where friend_info.user_id = #{uid} ")
    List<FriendInfo> getFriendList(int uid);

    @Select("select * from friend_info where friend_info.user_id = #{uid} " +
            "AND friend_info.friend_id = #{fid}")
    FriendInfo getFriend(int uid, int fid);
}
