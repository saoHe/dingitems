package com.ding.dingitems.meeting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ding.dingitems.meeting.entity.DdUser;
import com.ding.dingitems.meeting.mapper.DdUserMapper;
import com.ding.dingitems.meeting.service.DdUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ding.dingitems.util.dingtalk.DingtalkHandle;
import com.ding.dingitems.util.exception.BusinessException;
import com.ding.dingitems.util.tools.PublicTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 钉钉用户 服务实现类
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
@Service
public class DdUserServiceImpl extends ServiceImpl<DdUserMapper, DdUser> implements DdUserService {

    @Autowired
    DingtalkHandle dingtalkHandle;

    @Override
    public JSONObject getDdUserId(String code, String accesstoken) {

        return null;
    }

    @Override
    public DdUser getDdUserInfo(String userId, String accesstoken) throws BusinessException {
        return null;
    }

    @Override
    public DdUser updateDduserByDdid(DdUser ddUser) throws BusinessException{
        DdUser ddUser2;
        QueryWrapper<DdUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("dd_id",ddUser.getDdId());
        String ddUserId;
        try {
            DdUser ddUserOld = getOne(queryWrapper);
            if (ddUserOld == null){
                ddUser.setId(PublicTools.generateShortUuid());
                ddUser.setCreateTime(LocalDateTime.now());
                save(ddUser);
            }else{
                ddUser.setId(ddUserOld.getId());
                updateById(ddUser);
            }
            ddUser2 = getById(ddUser.getId());
        }catch (Exception e){
            throw new BusinessException("用户Ddid可能重复");
        }
        return ddUser2;
    }
}
