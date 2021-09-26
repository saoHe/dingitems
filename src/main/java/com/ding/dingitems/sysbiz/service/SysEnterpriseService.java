package com.ding.dingitems.sysbiz.service;

import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ding.dingitems.util.exception.BusinessException;

/**
 * <p>
 * 钉钉企业账户配置 服务类
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
public interface SysEnterpriseService extends IService<SysEnterprise> {

    /**
     * 根据corpId获取企业系统对象
     * @param corpId
     * @return
     * @throws BusinessException
     */
    SysEnterprise getEnteriseInfo(String corpId) throws BusinessException;

    /**
     * 获取企业系统的 可用accesstoken
     * @param sysEnterprise
     * @return
     * @throws BusinessException
     */
    String getAccesstoken(SysEnterprise sysEnterprise) throws BusinessException;




}
