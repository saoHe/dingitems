package com.ding.dingitems.sysbiz.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ding.dingitems.meeting.entity.DdUser;
import com.ding.dingitems.meeting.service.DdUserService;
import com.ding.dingitems.sysbiz.dto.ApiReturnResult;
import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.ding.dingitems.sysbiz.service.SysEnterpriseService;
import com.ding.dingitems.util.dingtalk.DingtalkHandle;
import com.ding.dingitems.util.exception.BusinessException;
import com.ding.dingitems.util.tools.PublicTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.events.EntityDeclaration;
import java.time.LocalDateTime;

/**
 * <p>
 * 钉钉企业账户配置 前端控制器
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@Slf4j
@RestController
@RequestMapping("main/sysbiz/")
public class SysEnterpriseController {

    @Autowired
    SysEnterpriseService sysEnterpriseService;
    @Autowired
    DdUserService ddUserService;
    @Autowired
    DingtalkHandle dingtalkHandle;

    /**
     * 用户code换用户信息
     *
     * @param corpId
     * @param code
     * @return
     */
    @RequestMapping(value = "/getToken")
    public ApiReturnResult getToken(String corpId, String code) throws Exception {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(corpId)) {
            throw new BusinessException("code，corpId is null");
        }
        // 根据corp_id查询企业 appkey和秘钥
        SysEnterprise sysEnterprise = sysEnterpriseService.getEnteriseInfo(corpId);
        System.out.println(JSON.toJSONString(sysEnterprise));
        // 获取access_token（service）
        String token = sysEnterpriseService.getAccesstoken(sysEnterprise);
        // 官方接口查询用户id
        JSONObject userjson = dingtalkHandle.getUserInfo(token,code);
        if (userjson == null) {
            throw new BusinessException("用户信息异常");
        }

        // 更新用戶信息
        DdUser ddUser = new DdUser().setDdId(PublicTools.generateShortUuid())
                .setDdName(userjson.getString("name"))
                .setDeviceId(userjson.getString("device_id"))
                .setEnterpriseId(sysEnterprise.getId())
                .setUpdateTime(LocalDateTime.now())
                .setIsSys(userjson.getBoolean("sys") ? 1 : 0)
                .setSysLevel(userjson.getInteger("sys_level"))
                .setUnionid(userjson.getString("unionid"))
                .setDdId(userjson.getString("userid"));
        DdUser ddUser1 = ddUserService.updateDduserByDdid(ddUser);
        ddUser1.setPassword(null);

        return ApiReturnResult.succ(ddUser1);
    }

}

