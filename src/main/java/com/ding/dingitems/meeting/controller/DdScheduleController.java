package com.ding.dingitems.meeting.controller;


import com.ding.dingitems.meeting.entity.DdSchedule;
import com.ding.dingitems.meeting.entity.DdUser;
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


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiReturnResult scheduleList(LocalDateTime queryDate,String userId) throws Exception {


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
}

