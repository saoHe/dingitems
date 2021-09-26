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
 * 钉钉用户
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dd_user")
public class DdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 钉钉用户id
     */
    @TableField("dd_id")
    private String ddId;

    /**
     * 姓名
     */
    @TableField("dd_name")
    private String ddName;

    @TableField("unionid")
    private String unionid;

    /**
     * 账户
     */
    @TableField("account")
    private String account;
    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 是否后台管理员 0否 1是
     */
    @TableField("isadmin")
    private Integer isadmin;

    /**
     * 加密后的密码
     */
    @TableField("password")
    private String password;

    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 是否企业内管理员 0否 1是
     */
    @TableField("is_sys")
    private Integer isSys;

    /**
     * 企业内级别 1：主管理员 2资管理员 100：老板  0：其他（如普通员工）
     */
    @TableField("sys_level")
    private Integer sysLevel;

    /**
     * 0：正常   -1：已删除 
     */
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
