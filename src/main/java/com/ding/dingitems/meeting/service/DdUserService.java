package com.ding.dingitems.meeting.service;

import com.alibaba.fastjson.JSONObject;
import com.ding.dingitems.meeting.entity.DdUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ding.dingitems.util.exception.BusinessException;

/**
 * <p>
 * 钉钉用户 服务类
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
public interface DdUserService extends IService<DdUser> {

    /**
     * 根据code从官方获取userId
     * @param code
     * @param accesstoken
     * @return
     * @throws BusinessException
     */
    JSONObject getDdUserId(String code, String accesstoken);

    /**
     * 官方获取用户详情，并保存入库
     * @param userId
     * @param accesstoken
     * @return
     * @throws BusinessException
     */
    DdUser getDdUserInfo(String userId,String accesstoken) throws BusinessException;


    /**
     * 根据Ddid 更新用户信息 或 新增
     * @param ddUser
     * @return
     */
    DdUser updateDduserByDdid(DdUser ddUser) throws BusinessException;


}
