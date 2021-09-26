package com.ding.dingitems.sysbiz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ding.dingitems.sysbiz.entity.SysEnterprise;
import com.ding.dingitems.sysbiz.mapper.SysEnterpriseMapper;
import com.ding.dingitems.sysbiz.service.SysEnterpriseService;
import com.ding.dingitems.util.dingtalk.DingtalkHandle;
import com.ding.dingitems.util.exception.BusinessException;
import com.ding.dingitems.util.redis.config.RedisComData;
import com.ding.dingitems.util.redis.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 钉钉企业账户配置 服务实现类
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@Service
public class SysEnterpriseServiceImpl extends ServiceImpl<SysEnterpriseMapper, SysEnterprise> implements SysEnterpriseService {

    @Autowired
    DingtalkHandle dingtalkHandle;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public SysEnterprise getEnteriseInfo(String corpId) throws BusinessException {

        QueryWrapper<SysEnterprise> queryWrapper = new QueryWrapper();
        queryWrapper.eq("corp_id", corpId);

        List<SysEnterprise> list = list(queryWrapper);
        if (!list.isEmpty() && list.size() != 1) {
            throw new BusinessException("企业信息异常或不存在");
        }
        return list.get(0);
    }

    @Override
    public String getAccesstoken(SysEnterprise sysEnterprise) throws BusinessException  {
        if (sysEnterprise == null || StringUtils.isBlank(sysEnterprise.getAppKey()) || StringUtils.isBlank(sysEnterprise.getAppSecret())) {
            throw new BusinessException("没有配置appkey或secrect");
        }
        String tokenStr = sysEnterprise.getAccessToken();

        // token时间过期，重新向官方获取
        if (sysEnterprise.getTokenTime() == null || sysEnterprise.getTokenTime().compareTo(LocalDateTime.now()) <= 0) {
            // 做防并发处理
            String iotRedis = "getAccesstoken";
            if (redisUtil.setnx(iotRedis + sysEnterprise.getId(), "1") > 0) {
                try {
                    // 设置11秒后失效，防止死锁
                    redisUtil.expire(iotRedis + sysEnterprise.getId(), 3, RedisComData.DATE_BASE_0.getCode());

                    JSONObject token = dingtalkHandle.getToken(sysEnterprise.getAppKey(), sysEnterprise.getAppSecret());
                    if (StringUtils.isBlank(token.getString("access_token"))) {
                        throw new BusinessException("apierror:" + token.getString("errmsg"));
                    }
                    tokenStr = token.getString("access_token");
                    long time = token.getLong("expires_in");
                    sysEnterprise.setAccessToken(tokenStr);
                    sysEnterprise.setTokenTime(LocalDateTime.now().plusSeconds(time - 60));
                    updateById(sysEnterprise);

                } finally {
                    redisUtil.del(RedisComData.DATE_BASE_0.getCode(), iotRedis + sysEnterprise.getId());
                }

            } else {
                try {// 如果同时进入，且没拿到锁，等待500ss后重新走方法判断时间
                    Thread.sleep(500);
                    getAccesstoken(sysEnterprise);
                }catch (InterruptedException e){
                    throw new BusinessException("延时加载异常");
                }

            }
        }

        return tokenStr;
    }

    public static void main(String[] args) {

        LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 22, 14, 14, 16);
        LocalDateTime localDateTime2 = LocalDateTime.of(2021, 9, 22, 14, 14, 15);
        System.out.println(localDateTime.compareTo(localDateTime2));
        System.out.println(localDateTime.plusSeconds(7200));
    }
}
