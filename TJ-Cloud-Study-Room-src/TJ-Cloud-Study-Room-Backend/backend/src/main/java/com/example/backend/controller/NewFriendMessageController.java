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
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping()
public class NewFriendMessageController {
    @Resource
    NewFriendMessageMapper newFriendMessageMapper;
    @Resource
    FriendInfoMapper friendInfoMapper;
    @Resource
    UserMapper userMapper;

    //发送添加好友消息 已发布
    @PostMapping("/friendsModule/sendFriendRequest")
    public Result<?> addFriend(@RequestParam Integer uid, @RequestParam Integer fid){
        NewFriendMessage newFriendMessage = new NewFriendMessage();
        newFriendMessage.setSenderId(uid);
        newFriendMessage.setReceiverId(fid);
        newFriendMessage.setSendTime(new Date());
        if(newFriendMessage.getSenderId() == null)
            return Result.error("-1", "缺少信息发送者id");
        if(newFriendMessage.getReceiverId() == null)
            return Result.error("-1", "缺少信息接受者id");
        newFriendMessage.setState(0);

        newFriendMessageMapper.insert(newFriendMessage);
        return Result.success();
    }

    @GetMapping("/newFriend/getMessage/Unprocessed")
    public Result<?> getUnprocessedMessage(@RequestParam Integer receiverId){
        List<NewFriendMessage> unprocessedMessagesList = newFriendMessageMapper.getUnprocessedMessage(receiverId);
        return Result.success(unprocessedMessagesList);
    }

    @GetMapping("/newFriend/getMessage/processed")
    public Result<?> getProcessedMessage(@RequestParam Integer receiverId){
        List<NewFriendMessage> processedMessagesList = newFriendMessageMapper.getProcessedMessage(receiverId);
        return Result.success(processedMessagesList);
    }

    //已发布
    @PostMapping("/newFriend/addToFriendInfo")
    public Result<?> addToFriendInfo(@RequestParam Integer senderId, @RequestParam Integer receiverId)
    {
        FriendInfo friendInfo1 = new FriendInfo();
        FriendInfo friendInfo2 = new FriendInfo();

        friendInfo1.setFriendId(senderId);
        friendInfo1.setUserId(receiverId);
        FriendInfo res1 = friendInfoMapper.selectOne(Wrappers.<FriendInfo>lambdaQuery().eq(FriendInfo::getFriendId, senderId)
                .eq(FriendInfo::getUserId, receiverId));
        //若数据库中没有这条好友信息，则加入
        if(res1 == null)
            friendInfoMapper.insert(friendInfo1);

        friendInfo2.setFriendId(receiverId);
        friendInfo2.setUserId(senderId);

        FriendInfo res2 = friendInfoMapper.selectOne(Wrappers.<FriendInfo>lambdaQuery().eq(FriendInfo::getFriendId, receiverId)
                .eq(FriendInfo::getUserId, senderId));
        if(res2 == null)
            friendInfoMapper.insert(friendInfo2);
        return Result.success();
    }

    //处理添加好友信息 已发布
    @PutMapping("/newFriend/process")
    public Result<?> process(@RequestParam Integer senderId,
                             @RequestParam Integer receiverId,@RequestParam Integer yes){

        //获得对应数据
        UpdateWrapper<NewFriendMessage> op = new UpdateWrapper<NewFriendMessage>();
        op.eq("sender_id", senderId).eq("receiver_id", receiverId);

        op.set("state", yes);
        newFriendMessageMapper.update(null, op);

        //如果同意，将好友信息添加到好友数据库
        if(yes == 1){
            addToFriendInfo(senderId, receiverId);
        }

        return Result.success();
    }

    //同意添加好友 已发布
    @PutMapping("/friendsModule/approveFriendRequest")
    public Result<?> agree(@RequestParam Integer uid,
                           @RequestParam Integer fid){
        process(fid, uid,1);
        return Result.success();
    }

    //拒绝添加好友 未发布
    @PutMapping("/friendsModule/rejectFriendRequest")
    public Result<?> reject(@RequestParam Integer uid,
                            @RequestParam Integer fid){
        process(fid,uid,-1);
        return Result.success();
    }

    //获取发送给我的好友申请
    @GetMapping("/friendsModule/getFriendRequests")
    public Result<?> getMyFriendMessage(@RequestParam Integer uid){
        List<NewFriendMessage> myFriendMessage = newFriendMessageMapper.getmyMessage(uid);

        List<FriendListItem> friendList =new ArrayList<>();

        for (int i = 0; i < myFriendMessage.size(); i++){
            User op = userMapper.selectById(myFriendMessage.get(i).getSenderId());
            FriendListItem item = new FriendListItem();
            item.id=myFriendMessage.get(i).getSenderId();
            item.online =op.getOnlineState();
            item.name = op.getUserName();
            item.avatar = op.getPicture();
            item.requestStatus = myFriendMessage.get(i).getState();
            item.requestTime = myFriendMessage.get(i).getSendTime();
            friendList.add(item);
        }
        return Result.success(friendList);
    }

    //获取我发送的好友申请
    @GetMapping("/friendsModule/getMyRequests")
    public Result<?> getMyRequest(@RequestParam Integer uid){
        List<NewFriendMessage> myFriendMessage = newFriendMessageMapper.getmyRequest(uid);

        List<FriendListItem> friendList =new ArrayList<>();

        for (int i = 0; i < myFriendMessage.size(); i++){
            User op = userMapper.selectById(myFriendMessage.get(i).getReceiverId());
            FriendListItem item = new FriendListItem();
            item.id=myFriendMessage.get(i).getReceiverId();
            item.online =op.getOnlineState();
            item.name = op.getUserName();
            item.avatar = op.getPicture();
            item.requestStatus = myFriendMessage.get(i).getState();
            item.requestTime = myFriendMessage.get(i).getSendTime();
            friendList.add(item);
        }
        return Result.success(friendList);
    }
}