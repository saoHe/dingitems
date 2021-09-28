package com.ding.dingitems.meeting.service.impl;

import com.alibaba.fastjson.JSON;
import com.ding.dingitems.meeting.entity.DdSchedule;
import com.ding.dingitems.meeting.mapper.DdScheduleMapper;
import com.ding.dingitems.meeting.service.DdScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.ding.dingitems.util.dingtalk.DingtalkHandle;
import com.ding.dingitems.util.exception.BusinessException;
import com.ding.dingitems.util.tools.PublicTools;
import com.dingtalk.api.request.OapiCalendarV2EventCreateRequest;
import com.dingtalk.api.request.OapiCalendarV2EventUpdateRequest;
import com.dingtalk.api.response.OapiCalendarV2EventCreateResponse;
import com.dingtalk.api.response.OapiCalendarV2EventUpdateResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 钉钉日程 服务实现类
 * </p>
 *
 * @author hjw
 * @since 2021-09-25
 */
@Service
public class DdScheduleServiceImpl extends ServiceImpl<DdScheduleMapper, DdSchedule> implements DdScheduleService {

    @Autowired
    DingtalkHandle dingtalkHandle;

    @Override
    public boolean saveDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException {

        OapiCalendarV2EventCreateRequest.Event event = new OapiCalendarV2EventCreateRequest.Event();
        List<OapiCalendarV2EventCreateRequest.Attendee> listAttendee = new ArrayList<>();

        listAttendee = Arrays.stream(ddSchedule.getAttendees().split(",")).map(a -> {
            OapiCalendarV2EventCreateRequest.Attendee attendee = new OapiCalendarV2EventCreateRequest.Attendee();
            attendee.setUserid(a);
            return attendee;
        }).collect(Collectors.toList());
        event.setAttendees(listAttendee);
        event.setCalendarId("primary");
        event.setDescription(ddSchedule.getDescription());

        OapiCalendarV2EventCreateRequest.DateTime dateTimestart = new OapiCalendarV2EventCreateRequest.DateTime();
        dateTimestart.setTimestamp(ddSchedule.getStartTime().toEpochSecond(ZoneOffset.of("+8")));
        event.setStart(dateTimestart);

        OapiCalendarV2EventCreateRequest.DateTime dateTimeend = new OapiCalendarV2EventCreateRequest.DateTime();
        dateTimeend.setTimestamp(ddSchedule.getEndTime().toEpochSecond(ZoneOffset.of("+8")));
        event.setEnd(dateTimeend);

        OapiCalendarV2EventCreateRequest.Attendee attendeeOrganizer = new OapiCalendarV2EventCreateRequest.Attendee();
        attendeeOrganizer.setUserid(ddSchedule.getOrganizerId());
        event.setOrganizer(attendeeOrganizer);

        event.setSummary(ddSchedule.getSummary());
        OapiCalendarV2EventCreateRequest.OpenCalendarReminderVo obj8 = new OapiCalendarV2EventCreateRequest.OpenCalendarReminderVo();
        obj8.setMethod("app");
        obj8.setMinutes(ddSchedule.getReminderMin().longValue());
        event.setReminder(obj8);

        OapiCalendarV2EventCreateRequest.LocationVo obj9 = new OapiCalendarV2EventCreateRequest.LocationVo();
        obj9.setLatitude(ddSchedule.getLatitude());
        obj9.setLongitude(ddSchedule.getLongitude());
        obj9.setPlace(ddSchedule.getAdress());
        event.setLocation(obj9);
        event.setNotificationType("NONE");

        ddSchedule.setDetails(JSON.toJSONString(event));

        OapiCalendarV2EventCreateResponse.Event eventRsp = dingtalkHandle.addSchedule(event, token, sysEnterprise.getAgentId());
        if (eventRsp != null) {
            ddSchedule.setEnterpriseId(sysEnterprise.getId());
            ddSchedule.setCreateTime(LocalDateTime.now());
            ddSchedule.setId(PublicTools.generateShortUuid());
            ddSchedule.setStatus(0);
            ddSchedule.setScheduleType(0);
            ddSchedule.setEventId(eventRsp.getEventId());
            return save(ddSchedule);
        }

        return false;
    }


    @Override
    public boolean updateDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException {

        OapiCalendarV2EventUpdateRequest.Event event = new OapiCalendarV2EventUpdateRequest.Event();

        if (StringUtils.isNotBlank(ddSchedule.getAttendees())) {
            List<OapiCalendarV2EventUpdateRequest.Attendee> listAttendee = new ArrayList<>();
            DdSchedule OldSchedule = getById(ddSchedule.getId());
            List<String> oldScheduleList = Arrays.asList(OldSchedule.getAttendees().split(","));

            // 新增的人
            Arrays.stream(ddSchedule.getAttendees().split(",")).forEach(a -> {
                OapiCalendarV2EventUpdateRequest.Attendee attendee = new OapiCalendarV2EventUpdateRequest.Attendee();
                boolean bool = oldScheduleList.stream().anyMatch(b -> b.equals(a));
                if (!bool) {
                    attendee.setUserid(a);
                    attendee.setAttendeeStatus("add");
                    listAttendee.add(attendee);
                }
            });

            // 删除的人
            oldScheduleList.forEach(a -> {
                OapiCalendarV2EventUpdateRequest.Attendee attendee = new OapiCalendarV2EventUpdateRequest.Attendee();
                boolean bool = Arrays.stream(ddSchedule.getAttendees().split(",")).anyMatch(b -> b.equals(a));
                if (!bool) {
                    attendee.setUserid(a);
                    attendee.setAttendeeStatus("remove");
                    listAttendee.add(attendee);
                }
            });
            event.setAttendees(listAttendee);

        }
        event.setCalendarId("primary");
        event.setDescription(ddSchedule.getDescription());

        OapiCalendarV2EventUpdateRequest.DateTime dateTimestart = new OapiCalendarV2EventUpdateRequest.DateTime();
        dateTimestart.setTimestamp(ddSchedule.getStartTime().toEpochSecond(ZoneOffset.of("+8")));
        event.setStart(dateTimestart);

        OapiCalendarV2EventUpdateRequest.DateTime dateTimeend = new OapiCalendarV2EventUpdateRequest.DateTime();
        dateTimeend.setTimestamp(ddSchedule.getEndTime().toEpochSecond(ZoneOffset.of("+8")));
        event.setEnd(dateTimeend);

        OapiCalendarV2EventUpdateRequest.Attendee attendeeOrganizer = new OapiCalendarV2EventUpdateRequest.Attendee();
        attendeeOrganizer.setUserid(ddSchedule.getOrganizerId());
        event.setOrganizer(attendeeOrganizer);

        event.setSummary(ddSchedule.getSummary());
        OapiCalendarV2EventUpdateRequest.OpenCalendarReminderVo obj8 = new OapiCalendarV2EventUpdateRequest.OpenCalendarReminderVo();
        obj8.setMethod("app");
        obj8.setMinutes(ddSchedule.getReminderMin().longValue());
        event.setReminder(obj8);

        OapiCalendarV2EventUpdateRequest.LocationVo obj9 = new OapiCalendarV2EventUpdateRequest.LocationVo();
        obj9.setLatitude(ddSchedule.getLatitude());
        obj9.setLongitude(ddSchedule.getLongitude());
        obj9.setPlace(ddSchedule.getAdress());
        event.setLocation(obj9);
        event.setEventId(ddSchedule.getEventId());

        ddSchedule.setDetails(JSON.toJSONString(event));

        boolean r = dingtalkHandle.editSchedule(event, token, sysEnterprise.getAgentId());
        if (r) {
            ddSchedule.setUpdateTime(LocalDateTime.now());
            return updateById(ddSchedule);
        }
        return false;
    }


    @Override
    public boolean delDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException {
        boolean r = dingtalkHandle.delSchedule(ddSchedule.getEventId(), token, sysEnterprise.getAgentId());
        if (r) {
            ddSchedule.setStatus(-1);
            ddSchedule.setUpdateTime(LocalDateTime.now());
            return updateById(ddSchedule);
        }
        return false;
    }




}
