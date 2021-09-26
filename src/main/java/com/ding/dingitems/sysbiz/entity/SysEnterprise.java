package com.ding.dingitems.sysbiz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 钉钉企业账户配置
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_enterprise")
public class SysEnterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("agent_id")
    private String agentId;

    @TableField("app_key")
    private String appKey;

    @TableField("app_secret")
    private String appSecret;

    /**
     * 2小时失效
     */
    @TableField("access_token")
    private String accessToken;

    /**
     * 获取最新token的时间，与目前时间差2小时要重新获取access_token
     */
    @TableField("token_time")
    private LocalDateTime tokenTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("corp_id")
    private String corpId;


}
