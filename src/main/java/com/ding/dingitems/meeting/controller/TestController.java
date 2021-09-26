package com.ding.dingitems.meeting.controller;


import com.alibaba.fastjson.JSON;
import com.ding.dingitems.meeting.entity.DdUser;
import com.ding.dingitems.meeting.mapper.DdUserMapper;
import com.ding.dingitems.meeting.service.DdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 * 钉钉用户 前端控制器
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@RestController
@RequestMapping("/main")
public class TestController {

    @Autowired
    DdUserService ddUserService;
    @Autowired
    DdUserMapper ddUserMapper;

    @RequestMapping(value = "/user/get")
    public String test(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("ddid","176833352520167713");
        DdUser u = ddUserMapper.selectUserByDdid(map);
        return JSON.toJSONString(u);
    }
}

