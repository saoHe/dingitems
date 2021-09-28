package com.ding.dingitems.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Transient;

/**
 * <p>
 * 钉钉会议室
 * </p>
 *
 * @author hjw
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dd_meeting")
public class DdMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 门禁id
     */
    @TableField("guard_id")
    private String guardId;

    /**
     * 名字
     */
    @TableField("m_name")
    private Integer mName;

    /**
     * 图片
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 会议室名字
     */
    @TableField("dd_name")
    private String ddName;

    /**
     * 地址
     */
    @TableField("adress")
    private String adress;

    /**
     * 容纳人数
     */
    @TableField("number")
    private Integer number;

    /**
     * 是否有电视 0否 1是
     */
    @TableField("tv")
    private Integer tv;

    /**
     * 是否有投影 0否 1是
     */
    @TableField("projection")
    private Integer projection;

    /**
     * 是否有白板 0否 1是
     */
    @TableField("board")
    private Integer board;

    /**
     * 是否有电话 0否 1是
     */
    @TableField("phone")
    private Integer phone;

    /**
     * 是否有视频 0否 1是
     */
    @TableField("video")
    private Integer video;

    /**
     * 0：正常   -1：已删除 
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @Transient
    private List<Integer> periodTime;

}
