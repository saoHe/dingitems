package com.ding.dingitems.meeting.mapper;

import com.ding.dingitems.meeting.entity.DdUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 钉钉用户 Mapper 接口
 * </p>
 *
 * @author hjw
 * @since 2021-09-21
 */
public interface DdUserMapper extends BaseMapper<DdUser> {

    DdUser selectUserByDdid(Map map);
}
