package com.ding.dingitems.util.dingtalk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ding.dingitems.meeting.entity.DdSchedule;
import com.ding.dingitems.util.exception.BusinessException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DingtalkHandle {

    /**
     * 获取应用内部token
     *
     * @param appkey
     * @param appsecret
     * @return succ：{"errcode":0,"access_token":"5252a09d02893c92819b02fc51448f47","errmsg":"ok","expires_in":7200}
     * erro:{"errcode":40096,"errmsg":"不合法的appKey或appSecret"}
     */
    public JSONObject getToken(String appkey, String appsecret) {
        OapiGettokenResponse response = new OapiGettokenResponse();
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(appkey);
            request.setAppsecret(appsecret);
            request.setHttpMethod("GET");
            response = client.execute(request);
        } catch (Exception e) {
            log.error("dingerror", e);
        }
        if (response != null && StringUtils.isNotBlank(response.getBody())) {
            return JSON.parseObject(response.getBody());
        } else {
            return null;
        }

    }

    /**
     * code换用户信息
     *
     * @param token
     * @param code
     * @return
     */
    public JSONObject getUserInfo(String token, String code) throws BusinessException {
        OapiV2UserGetuserinfoResponse rsp = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
            OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
            req.setCode(code);
            rsp = client.execute(req, token);
        } catch (Exception e) {
            log.error("dingerror", e);
        }
        if (rsp == null) {
            throw new BusinessException("官方获取用户出现异常");
        }
        if (rsp.getErrcode() == 0 && StringUtils.isNotBlank(rsp.getBody())) {
            JSONObject jsonObject = JSON.parseObject(rsp.getBody()).getJSONObject("result");
            return jsonObject;
        } else {
            throw new BusinessException("错误：" + JSON.parseObject(rsp.getBody()).getString("errmsg"));
        }

    }

    /**
     * 创建日程
     *
     * @param token
     * @return
     */
    public OapiCalendarV2EventCreateResponse.Event addSchedule(OapiCalendarV2EventCreateRequest.Event event, String token, String agentId) throws BusinessException {
        OapiCalendarV2EventCreateResponse rsp = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/calendar/v2/event/create");
            OapiCalendarV2EventCreateRequest req = new OapiCalendarV2EventCreateRequest();
            req.setEvent(event);
            req.setAgentid(Long.valueOf(agentId));
            rsp = client.execute(req, token);
        } catch (Exception e) {
            log.error("dingerror", e);
        }
        if (rsp == null) {
            throw new BusinessException("官方获取用户出现异常");
        }
        if (rsp.getErrcode() == 0 && StringUtils.isNotBlank(rsp.getBody())) {
            return rsp.getResult();
        } else {
            throw new BusinessException("错误：" + rsp.getErrmsg());
        }

    }


    /**
     * 修改日程
     *
     * @param token
     * @return
     */
    public boolean editSchedule(OapiCalendarV2EventUpdateRequest.Event event, String token, String agentId) throws BusinessException {
        OapiCalendarV2EventUpdateResponse rsp = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/calendar/v2/event/update");
            OapiCalendarV2EventUpdateRequest req = new OapiCalendarV2EventUpdateRequest();
            req.setEvent(event);
            req.setAgentid(Long.valueOf(agentId));
            rsp = client.execute(req, token);
        } catch (Exception e) {
            log.error("dingerror", e);
        }
        if (rsp == null) {
            throw new BusinessException("官方获取用户出现异常");
        }
        if (rsp.getErrcode() == 0 && rsp.getSuccess()) {
            return true;
        } else {
            throw new BusinessException("错误：" + rsp.getErrmsg());
        }

    }

    /**
     * 取消日程
     *
     * @param token
     * @return
     */
    public boolean delSchedule(String eventId, String token, String agentId) throws BusinessException {
        OapiCalendarV2EventCancelResponse rsp = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/calendar/v2/event/cancel");
            OapiCalendarV2EventCancelRequest req = new OapiCalendarV2EventCancelRequest();
            req.setCalendarId("primary");
            req.setEventId(eventId);
            req.setAgentid(Long.valueOf(agentId));
            rsp = client.execute(req, token);
        } catch (Exception e) {
            log.error("dingerror", e);
        }
        if (rsp == null) {
            throw new BusinessException("官方获取用户出现异常");
        }
        if (rsp.getErrcode() == 0 && rsp.getSuccess()) {
            return true;
        } else {
            throw new BusinessException("错误：" + rsp.getErrmsg());
        }

    }


    public static void main(String[] args) throws BusinessException {
        DingtalkHandle dingtalkHandle = new DingtalkHandle();
//        JSON r = dingtalkHandle.getToken("dingmpw0lh0slklcu8dk", "qe8i0lKxBKmR4Bz27WaU08SJa0VqU25erLDkUztyXmAuJVjoExjNAnJBjNVH2TMK");
//        System.out.println(JSON.toJSONString(r));
        String appkey = "dingmpw0lh0slklcu8dk";
        String appsecret = "qe8i0lKxBKmR4Bz27WaU08SJa0VqU25erLDkUztyXmAuJVjoExjNAnJBjNVH2TMK";
        String token = "c3086214bd153041bef52f5ce67071d8";
        String agentId = "1307471491";


        DdSchedule ddSchedule = new DdSchedule();
        ddSchedule.setDescription("测试日程");
        ddSchedule.setSummary("一起开大会吧2");
        ddSchedule.setReminderMin(5);
        ddSchedule.setStartTime(LocalDateTime.of(2021, 9, 25, 16, 50, 00));
        ddSchedule.setEndTime(LocalDateTime.of(2021, 9, 25, 17, 00, 00));
        ddSchedule.setAttendees("176833352520167713");
        ddSchedule.setOrganizerId("176833352520167713");
        ddSchedule.setDdUserId("1");
        ddSchedule.setDdMeetingId("123");
        ddSchedule.setAdress("常州大剧院");
        ddSchedule.setLatitude("123.232");
        ddSchedule.setLongitude("213.312");
        System.out.println(JSON.toJSONString(ddSchedule));

//        OapiCalendarV2EventCreateResponse.Event event1 = dingtalkHandle.addSchedule(event, token, agentId);
//        System.out.println(JSON.toJSONString(event1));
//
//        OapiCalendarV2EventUpdateRequest.Event updateEvent = new OapiCalendarV2EventUpdateRequest.Event();
//        BeanUtils.copyProperties(event1, updateEvent);
//        System.out.println(updateEvent);
//
//        OapiCalendarV2EventUpdateRequest.Attendee attendee = new OapiCalendarV2EventUpdateRequest.Attendee();
//        attendee.setUserid("12435443321156276");
//        updateEvent.getAttendees().add(attendee);
//        dingtalkHandle.editSchedule(updateEvent,token,agentId);

    }
}
