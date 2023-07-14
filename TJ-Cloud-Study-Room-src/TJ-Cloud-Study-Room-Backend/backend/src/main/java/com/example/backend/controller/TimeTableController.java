package com.example.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.TimeTable;
import com.example.backend.mapper.TimeTableMapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping()

public class TimeTableController {
    @Resource
    TimeTableMapper timeTableMapper;

    @PostMapping("/userCenter/timetable")               /* 实现日程新增 */
    public Result<?> add(@RequestBody TimeTable timeTable) {
        timeTableMapper.insert(timeTable);
        return Result.success();
    }

    @PutMapping("/userCenter/timetable")                /* 实现日程更新 */
    public Result<?> updateByUser(@RequestBody TimeTable timeTable) {
        timeTableMapper.updateById(timeTable);
        return Result.success();
    }

    @DeleteMapping("/userCenter/timetable")             /* 实现日程删除 */
    public Result<?> delete(@RequestParam Integer cid) {
        timeTableMapper.deleteById(cid);
        return Result.success();
    }

    @GetMapping("/userCenter/timetable")
    public Result<?> findClass(@RequestParam(defaultValue = "") Integer search) {
        LambdaQueryWrapper<TimeTable> wrapper = Wrappers.<TimeTable>lambdaQuery();

        List<TimeTable> res = timeTableMapper.selectList(wrapper.eq(TimeTable::getUid, search));

        return Result.success(res);
    }
}
