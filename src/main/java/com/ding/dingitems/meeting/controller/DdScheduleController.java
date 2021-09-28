package com.ding.dingitems.meeting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ding.dingitems.meeting.entity.DdMeeting;
import com.ding.dingitems.meeting.entity.DdSchedule;
import com.ding.dingitems.meeting.entity.DdUser;
import com.ding.dingitems.meeting.service.DdMeetingService;
import com.ding.dingitems.meeting.service.DdScheduleService;
import com.ding.dingitems.meeting.service.DdUserService;
import com.ding.dingitems.sysbiz.dto.ApiReturnResult;
import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.ding.dingitems.sysbiz.service.SysEnterpriseService;
import com.ding.dingitems.util.dingtalk.DingtalkHandle;
import com.ding.dingitems.util.dto.schedule.InParameter;
import com.ding.dingitems.util.exception.BusinessException;
import com.dingtalk.api.request.OapiCalendarV2EventCreateRequest;
import com.dingtalk.api.response.OapiCalendarV2EventCreateResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.BuilderException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 钉钉日程 前端控制器
 * </p>
 *
 * @author hjw
 * @since 2021-09-25
 */
@RestController
@RequestMapping("/main/schedule")
public class DdScheduleController {

    @Autowired
    DdUserService ddUserService;
    @Autowired
    SysEnterpriseService sysEnterpriseService;
    @Autowired
    DingtalkHandle dingtalkHandle;
    @Autowired
    DdScheduleService ddScheduleService;
    @Autowired
    DdMeetingService ddMeetingService;

    // todo 按条件查找会议室
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiReturnResult scheduleList(LocalDateTime queryDate, String userId) throws Exception {
        if (StringUtils.isBlank(userId) || queryDate == null) {
            throw new BusinessException("参数确少");
        }
        LocalDateTime start = LocalDateTime.of(queryDate.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(queryDate.toLocalDate(), LocalTime.MAX);
        DdUser ddUser = ddUserService.getById(userId);
        if (ddUser == null) {
            throw new BusinessException("用户信息异常");
        }

        //查出所有会议室
        QueryWrapper<DdMeeting> queryWrapper = new QueryWrapper();
        queryWrapper.eq("enterprise_id", ddUser.getEnterpriseId());
        queryWrapper.eq("status", 0);
        List<DdMeeting> list = ddMeetingService.list(queryWrapper);
        if (!list.isEmpty()) {
            // 查出所有相关日程
            QueryWrapper<DdSchedule> queryWrapperSc = new QueryWrapper();
            queryWrapperSc.eq("enterprise_id", ddUser.getEnterpriseId());
            queryWrapperSc.eq("status", 0);
            queryWrapperSc.eq("schedule_type", 0);
            queryWrapperSc.le("start_time", DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(end));
            queryWrapperSc.ge("start_time", DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(start));
            queryWrapperSc.orderByAsc("start_time");
            List<DdSchedule> listSc = ddScheduleService.list(queryWrapperSc);
            if (!listSc.isEmpty()) {
                list.forEach(d -> {
                    // 获得当前会议室日程列表
                    List<DdSchedule> praiveList = listSc.stream().filter(a -> a.getDdMeetingId().equals(d.getId())).collect(Collectors.toList());
                    //获得 每半小时数组的状态算法
                    if (!praiveList.isEmpty()) {
                        int now = LocalDateTime.now().getHour() + LocalDateTime.now().getMinute();
                        // 最后状态list
                        List<Integer> list1 = new ArrayList<>();
                        int [] rarry = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
                        // 初始早上7点开始算
                        final int startM = 7 * 60;

                        // 代表状态位数，7到23点每半小时一共32个点
                        int num = 0;
                        praiveList.forEach(p -> {
                            int pM = p.getStartTime().getHour() + p.getStartTime().getMinute();
                            if (p.getDdUserId().equals(userId)) {
                                list1.add(0);// 自己预约的
                            } else {
                                list1.add(1);// 别人预约的
                                if (pM < now) {
                                    list1.add(2);// 已过期
                                }
                            }
//                            startM += 30;
                        });
                    }
                });
            }
        }
        //查出会议室对应的预约时间

        return ApiReturnResult.succ("1");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiReturnResult scheduleCreate(@RequestBody DdSchedule parameter) throws Exception {

        if (parameter == null || StringUtils.isBlank(parameter.getDdUserId())) {
            throw new BusinessException("参数错误");
        }

        DdUser ddUser = ddUserService.getById(parameter.getDdUserId());
        if (ddUser == null || ddUser.getEnterpriseId() == null) {
            throw new BusinessException("用户不存在");
        }
        SysEnterprise sysEnterprise = sysEnterpriseService.getById(ddUser.getEnterpriseId());
        // 获取access_token（service）
        String token = sysEnterpriseService.getAccesstoken(sysEnterprise);
        boolean r = ddScheduleService.saveDdSchedule(parameter, token, sysEnterprise);

        return ApiReturnResult.succ(r);

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiReturnResult scheduleUpdate(@RequestBody DdSchedule parameter) throws Exception {

        if (parameter == null || StringUtils.isBlank(parameter.getDdUserId()) || StringUtils.isBlank(parameter.getId()) || StringUtils.isBlank(parameter.getEventId())) {
            throw new BusinessException("参数错误");
        }

        DdUser ddUser = ddUserService.getById(parameter.getDdUserId());
        if (ddUser == null || ddUser.getEnterpriseId() == null) {
            throw new BusinessException("用户不存在");
        }
        SysEnterprise sysEnterprise = sysEnterpriseService.getById(ddUser.getEnterpriseId());
        // 获取access_token（service）
        String token = sysEnterpriseService.getAccesstoken(sysEnterprise);
        boolean r = ddScheduleService.updateDdSchedule(parameter, token, sysEnterprise);

        return ApiReturnResult.succ(r);

    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ApiReturnResult scheduleDel(@RequestBody DdSchedule parameter) throws Exception {

        if (parameter == null || StringUtils.isBlank(parameter.getDdUserId()) || StringUtils.isBlank(parameter.getId()) || StringUtils.isBlank(parameter.getEventId())) {
            throw new BusinessException("参数错误");
        }

        DdUser ddUser = ddUserService.getById(parameter.getDdUserId());
        if (ddUser == null || ddUser.getEnterpriseId() == null) {
            throw new BusinessException("用户不存在");
        }
        SysEnterprise sysEnterprise = sysEnterpriseService.getById(ddUser.getEnterpriseId());
        // 获取access_token（service）
        String token = sysEnterpriseService.getAccesstoken(sysEnterprise);
        boolean r = ddScheduleService.delDdSchedule(parameter, token, sysEnterprise);

        return ApiReturnResult.succ(r);

    }

    public static void main(String[] args) {
        //开始算当前时间过期的
        LocalDateTime t1 = LocalDateTime.of(2021, 9, 26, 8, 25, 0);
        // 时间段，默认未预约 2
        String [] rarry = {"2", "2", "2", "2", "2", "2", "2","2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"};
        int start = 7 * 60;
        int now = t1.getHour() * 60 + t1.getMinute();
        if ((now-start)>0) {
            int i = (int) ((now - start) / 30);
            for (int j = 0; j < i; j++) {
                rarry[j] = "0";
            }
        }
        String s = StringUtils.join(rarry,",");
        System.out.println(s);

        //算预约时间段

        LocalDateTime t2 = LocalDateTime.of(2021, 9, 26, 8, 30, 0);
        LocalDateTime t3 = LocalDateTime.of(2021, 9, 26, 9, 30, 0);
        int st = t2.getHour() * 60 + t2.getMinute();
        int et = t3.getHour() * 60 + t3.getMinute();

        int startInt = (int)((st - start)/30);
        int q =(int)((et - st)/30) + startInt;
        for (int j = startInt; j <= q-1; j++) {
            rarry[j] = "1";
        }
        String s1 = StringUtils.join(rarry,",");
        System.out.println(s1);


        //算预约时间段

        LocalDateTime t4 = LocalDateTime.of(2021, 9, 26, 21, 00, 0);
        LocalDateTime t5 = LocalDateTime.of(2021, 9, 26, 22, 30, 0);
        int st4 = t4.getHour() * 60 + t4.getMinute();
        int et4 = t5.getHour() * 60 + t5.getMinute();

        int startInt4 = (int)((st4 - start)/30);
        int q4 =(int)((et4 - st4)/30) + startInt4;
        for (int j = startInt4; j <= q4-1; j++) {
            rarry[j] = "1";
        }
        String s4 = StringUtils.join(rarry,",");
        System.out.println(s4);

    }
}

