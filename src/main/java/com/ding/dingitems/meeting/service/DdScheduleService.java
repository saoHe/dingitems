package com.ding.dingitems.meeting.service;

import com.ding.dingitems.meeting.entity.DdSchedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.ding.dingitems.util.exception.BusinessException;
import com.dingtalk.api.response.OapiCalendarV2EventCreateResponse;

/**
 * <p>
 * 钉钉日程 服务类
 * </p>
 *
 * @author hjw
 * @since 2021-09-25
 */
public interface DdScheduleService extends IService<DdSchedule> {

    boolean saveDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException;

    boolean updateDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException;

    boolean delDdSchedule(DdSchedule ddSchedule, String token, SysEnterprise sysEnterprise) throws BusinessException;
}
