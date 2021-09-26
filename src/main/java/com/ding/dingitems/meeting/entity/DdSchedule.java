package com.ding.dingitems.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 钉钉日程
 * </p>
 *
 * @author hjw
 * @since 2021-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dd_schedule")
public class DdSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 日程类型 0：会议
     */
    @TableField("schedule_type")
    private Integer scheduleType;

    /**
     * 会议室id
     */
    @TableField("dd_meeting_id")
    private String ddMeetingId;

    /**
     * 组织者id
     */
    @TableField("organizer_id")
    private String organizerId;

    /**
     * 用户id
     */
    @TableField("dd_user_id")
    private String ddUserId;

    /**
     * 日程主题
     */
    @TableField("summary")
    private String summary;

    /**
     * 参与者user_id 逗号分割
     */
    @TableField("attendees")
    private String attendees;

    /**
     * 日程ID
     */
    @TableField("event_id")
    private String eventId;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 开始前提醒（单位分钟）
     */
    @TableField("reminder_min")
    private Integer reminderMin;

    /**
     * 地址
     */
    @TableField("adress")
    private String adress;

    /**
     * 0：正常   -1：已取消
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 阿里订单id
     */
    @TableField("ali_order_id")
    private String aliOrderId;

    /**
     * 日程描述
     */
    @TableField("description")
    private String description;

    /**
     * 纬度
     */
    @TableField("latitude")
    private String latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private String longitude;

    /**
     * 日程详情Json
     */
    @TableField("details")
    private String details;


}
