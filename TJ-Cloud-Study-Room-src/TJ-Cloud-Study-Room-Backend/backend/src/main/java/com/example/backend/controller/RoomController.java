package com.example.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.Room;
import com.example.backend.entity.User;
import com.example.backend.mapper.RoomMapper;
import com.example.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping()
public class RoomController {
    @Resource
    RoomMapper roomMapper;
    @Resource
    UserMapper userMapper;
    //admin add room
    @PostMapping("/admin/room")
    public Result<?> adminSaveRoom(@RequestBody Room room){
        if(room.getRoomName() == null)
            return Result.error("-1", "未设置房间名");
        if(room.getMaxNum() == null)
            room.setMaxNum(20);
        if(room.getRoomAnnouncement()==null)
            room.setRoomAnnouncement("无");
        if(room.getRoomPassword()==null)
            room.setRoomPassword("-1");
        room.setStartTime(new Date());
        room.setFixed(0);           //the room added by user is not a fixed room
        room.setPreNum(0);

        roomMapper.insert(room);
        return Result.success();
    }
    //admin search room
    @GetMapping("/admin/room")
    public Result<?> adminFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Room> wrapper = Wrappers.<Room>lambdaQuery();
        if(StrUtil.isNotBlank(search)) {
            wrapper.like(Room::getRoomName, search);
        }
        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(roomPage);
    }

    //admin update room info(edit room info)
    @PutMapping("/admin/room")
    public Result<?> adminUpdateRoom(@RequestBody Room room){
        if(room.getRoomName() == null || room.getRoomName() == ""){
            return Result.error("-1", "未设置房间名");
        }
        roomMapper.updateById(room);
        return Result.success();
    }

    //admin delete room
    @DeleteMapping("/admin/room/{uid}")
    public Result<?> adminDeleteRoom(@PathVariable Integer uid){
        roomMapper.deleteById(uid);
        return Result.success();
    }

    //get user num
    @GetMapping("/studyHall/getOnlineUserNum")
    public Result<?> getUserNum(){
        int userOnlineNum = userMapper.getUserNum();
        return Result.success(userOnlineNum);
    }

    //user in studyHall add room
    @PostMapping("/studyHall")
    public Result<?> userSaveRoom(@RequestBody Room room){
        if(room.getRoomName() == null)
            return Result.error("-1", "未设置房间名");
        if(room.getMaxNum() == null)
            room.setMaxNum(20);
        if(room.getRoomAnnouncement()==null)
            room.setRoomAnnouncement("");
        if(room.getRoomPassword()==null)
            room.setRoomPassword("-1");
        room.setStartTime(new Date());
        room.setFixed(0);           //the room added by user is not a fixed room
        room.setPreNum(0);

        roomMapper.insert(room);
        return Result.success(room);
    }

    //user in studyHall search room
    @GetMapping("/studyHall")
    public Result<?> userFindPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Room> wrapper = Wrappers.<Room>lambdaQuery();
        if(StrUtil.isNotBlank(search)) {
            wrapper.like(Room::getRoomName, search);
        }
        Page<Room> roomPage = roomMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
//        //时间格式化
//        // 定义时间格式化对象
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 循环执行时间格式化
//        roomPage.getRecords().forEach(item -> {
//            // 使用预留字段 ctime 接收 createtime 格式化的时间(Date->String)
//            item.setStartTime(dateFormat.format(item.getStartTime()));
//            item.setUtime(dateFormat.format(item.getUpdatetime()));
//        });
        return Result.success(roomPage);
    }

    //update user info when user enters room
    @PostMapping("/studyHall/enterRoom/updateUserInfo")
    public Result<?> userEnterRoomUpdateUserInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        UpdateWrapper<User> op = new UpdateWrapper<User>();
        op.eq("uid", uid);
        op.set("room_enter_time", new Date());
        op.set("room_id", rid);
        userMapper.update(null, op);

        return Result.success();
    }

    //update room info when user in studyhall enter room
    @PostMapping("/studyHall/enterRoom/updateRoomInfo")
    public Result<?> userEnterRoomUpdateRoomInfo(@RequestParam Integer uid,
                                                 @RequestParam Integer rid){
        UpdateWrapper<Room> op = new UpdateWrapper<Room>();
        op.eq("rid", rid);

        op.setSql("pre_num = pre_num + 1");
        roomMapper.update(null, op);

        return Result.success();
    }

    //user in studyHall enter room
    @PostMapping("/studyHall/checkpsw/{rid}/{psw}/{uid}")
    public Result<?> userEnterRoom(@PathVariable Integer rid,
                                   @PathVariable String psw,
                                   @PathVariable Integer uid) {
        User user=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid, uid));
        if(user.getRoomId() != 0)
            return Result.error("-2", "您已在房间中，请先退出房间");

        Room res = roomMapper.selectOne(
                Wrappers.<Room>lambdaQuery().eq(Room::getRid, rid).eq(Room::getRoomPassword, psw)
        );

        if(res == null)
            return Result.error("-1", "房间密码错误");
        else
        {
            userEnterRoomUpdateRoomInfo(uid, rid);
            userEnterRoomUpdateUserInfo(uid, rid);
            res = roomMapper.selectById(rid);

            return Result.success(res);
        }
    }

    //get user and room by uid and rid
    @GetMapping("/rooms/getUserInfo/id")
    public Result<?> getUserInfoById(@RequestParam Integer uid){
        User user = userMapper.selectById(uid);
        return Result.success(user);
    }
    @GetMapping("/rooms/getUserInfo/name")
    public Result<?> getUserInfoByName(@RequestParam String userName){
        User user = userMapper.getUserByUname(userName);
        return Result.success(user);
    }

    //get userList by rid
    //未测试
    @GetMapping("/admin/room/getUserList")
    public Result<?> getUserListByRid(@RequestParam Integer rid){
        List<User> userList = userMapper.selectUidByRid(rid);
        return Result.success(userList);
    }

    //user in study room exit room (王乐宣使用）
    @PutMapping("/rooms/exit")
    public Result<?> userExitRoom(@RequestParam Integer uid,
                                  @RequestParam Integer rid){
        UpdateWrapper<Room> room = new UpdateWrapper<Room>();
        room.eq("rid", rid);

        if(room!=null){
            room.setSql("pre_num = pre_num - 1");
            roomMapper.update(null, room);
        }

        UpdateWrapper<User> user = new UpdateWrapper<User>();
        user.eq("uid", uid);

        user.set("room_id", 0);
        userMapper.update(null, user);

        return Result.success();
    }

    //获取房间信息
    @GetMapping("/rooms/getRoomInfo")
    public  Result<?> getRoomInfo(@RequestParam Integer rid)
    {
        Room room = roomMapper.selectById(rid);

        if(room==null)
            return Result.error("-1", "房间已解散！");

        return Result.success(room);
    }

    @PutMapping("/rooms/changeRoomInfo")
    public Result<?> userEditRoomInfo(@RequestBody Room room)
    {
        roomMapper.updateById(room);
        return Result.success();
    }
    @DeleteMapping("/rooms/deleteRoom/{rid}")
    public Result<?> deleteRoom(@PathVariable Integer rid)
    {
        roomMapper.deleteById(rid);
        return Result.success();
    }
}
