package com.example.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.*;
import com.example.backend.mapper.FriendInfoMapper;
import com.example.backend.mapper.NewFriendMessageMapper;
import com.example.backend.mapper.TimeTableMapper;
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class FriendInfoController {
    @Resource
    FriendInfoMapper friendInfoMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    TimeTableMapper timeTableMapper;
    //获取好友列表
    @GetMapping("/friendsModule/friendList")
    public Result<?> getFriendList(@RequestParam Integer uid){
        List<FriendInfo> friendInfoList = friendInfoMapper.getFriendList(uid);
        List<FriendListItem> friendList =new ArrayList<>();

        for (int i=0;i<friendInfoList.size();i++){
            User op = userMapper.selectById(friendInfoList.get(i).getFriendId());
            FriendListItem item = new FriendListItem();
            item.id=friendInfoList.get(i).getFriendId();
            item.online =op.getOnlineState();
            item.name = op.getUserName();
            item.avatar = op.getPicture();
            friendList.add(item);
        }
        return Result.success(friendList);
    }
    //删除好友
    @DeleteMapping("/friendsModule/deleteFriends/{uid}/{fid}")
    public Result<?> deleteFriend(@PathVariable Integer uid, @PathVariable Integer fid){
        QueryWrapper<FriendInfo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id",uid).eq("friend_id",fid);
        friendInfoMapper.delete(wrapper1);

        QueryWrapper<FriendInfo> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id",fid).eq("friend_id",uid);
        friendInfoMapper.delete(wrapper2);

        return Result.success();
    }
    //获取用户列表（根据用户名)
    @GetMapping("/friendsModule/addFriendSearchList")
    public Result<?> findPage(@RequestParam(defaultValue = "") String fname, @RequestParam Integer uid) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(fname)) {
            wrapper.like(User::getUserName, fname);
        }
        List<User> userList = userMapper.getUserListByUname(fname);

        List<FriendListItem> friendList =new ArrayList<>();
        FriendListItem item = new FriendListItem();

        for(int i=0;i<userList.size();i++){
            item.id = userList.get(i).getUid();
            item.avatar = userList.get(i).getPicture();
            item.name = userList.get(i).getUserName();
            item.online = userList.get(i).getOnlineState();

            FriendInfo res1 = friendInfoMapper.selectOne(Wrappers.<FriendInfo>lambdaQuery().eq(FriendInfo::getFriendId, uid)
                    .eq(FriendInfo::getUserId, userList.get(i).getUid()));

            if(res1==null)
                item.isFriend = false;
            else
                item.isFriend = true;

            friendList.add(item);
        }
        return Result.success(friendList);
    }

    //推荐好友(根据重叠的课数最多来进行推荐）
    @GetMapping("/recommand/friend")
    public Result<?> recommand(@RequestParam Integer uid){
        //获得被推荐人的课表
        List<TimeTable> userTimeTable = timeTableMapper.getUserTimeTable(uid);
        List<List<TimeTable>> othersTimeTable = new ArrayList<>();

        //获得所有用户列表
        List<User> userList = timeTableMapper.getUser();

        //获得所有用户的课表
        for(int i=0;i<userList.size();i++){
            othersTimeTable.add(timeTableMapper.getUserTimeTable(userList.get(i).getUid()));
        }

        //将每个用户与被推荐用户相同的课程数目放入SameCourseNum数组，已经考虑了将被推荐者排除在外
        List<Integer> SameCourseNum = new ArrayList<>();
        for(int i=0;i<userList.size();i++){
            int sameCourseNum = 0;
            if(userList.get(i).getUid()==uid){
                SameCourseNum.add(sameCourseNum);
                continue;
            }
            for(int j=0;j<userTimeTable.size();j++){
                for(int k=0;k<othersTimeTable.get(i).size();k++){
                    if(othersTimeTable.get(i).get(k).getStart()==userTimeTable.get(j).getStart()
                            && othersTimeTable.get(i).get(k).getEnd()==userTimeTable.get(j).getEnd()
                            && othersTimeTable.get(i).get(k).getWeekday()==userTimeTable.get(j).getWeekday()){
                        sameCourseNum++;
                        break;
                    }
                }
            }
            SameCourseNum.add(sameCourseNum);
        }

        //用三次循环，依次找到课程数目相同的前三名非好友用户，放到goalUserList中
        int max = 0;
        int goalid1 = 0;
        int goalIndex1=0;
        List<User> goalUserList = new ArrayList<>();
        //第一次
        for(int i=0;i<userList.size();i++){
            FriendInfo t = friendInfoMapper.getFriend(uid, userList.get(i).getUid());
            if(t != null)
                continue;
            if(SameCourseNum.get(i)>max){
                goalIndex1 = i;
                goalid1 = userList.get(i).getUid();
            }

        }
        goalUserList.add(userMapper.selectById(goalid1));
        //第二次
        int goalid2 = 0;
        int goalIndex2 = 0;
        for(int i=0;i<userList.size();i++){
            FriendInfo t = friendInfoMapper.getFriend(uid, userList.get(i).getUid());
            if(t != null)
                continue;
            if(SameCourseNum.get(i)>max && userList.get(i).getUid()!=goalid1){
                goalIndex2 = i;
                goalid2 = userList.get(i).getUid();
            }
        }
        if(goalid2!=0)
            goalUserList.add(userMapper.selectById(goalid2));
        //第三次
        int goalid3 = 0;
        int goalIndex3 = 0;
        for(int i=0;i<userList.size();i++){
            FriendInfo t = friendInfoMapper.getFriend(uid, userList.get(i).getUid());
            if(t != null)
                continue;
            if(SameCourseNum.get(i)>max && userList.get(i).getUid()!=goalid1 && userList.get(i).getUid()!=goalid2){
                goalid3 = userList.get(i).getUid();
                goalIndex3 = i;
            }
        }
        if(goalid3!=0)
            goalUserList.add(userMapper.selectById(goalid3));

        //创建list作为返回数组。
        List<FriendListItem> list = new ArrayList<>();
        FriendListItem e1 = new FriendListItem();
        FriendListItem e2 = new FriendListItem();
        FriendListItem e3 = new FriendListItem();

        //将找到的第一名放入list
        e1.id = userList.get(goalIndex1).getUid();
        e1.avatar = userList.get(goalIndex1).getPicture();
        e1.name=userList.get(goalIndex1).getUserName();
        list.add(e1);

        //如果有，将找到的第二个放入list
        if(goalid2!=0){
            e2.id = userList.get(goalIndex2).getUid();
            e2.avatar = userList.get(goalIndex2).getPicture();
            e2.name=userList.get(goalIndex2).getUserName();
            list.add(e2);
        }
        //如果有，将找到的第三个放入list
        if(goalid3!=0){
            e3.id = userList.get(goalIndex3).getUid();
            e3.avatar = userList.get(goalIndex3).getPicture();
            e3.name=userList.get(goalIndex3).getUserName();
            list.add(e3);
        }

        //返回结果
        return Result.success(list);

    }
}
