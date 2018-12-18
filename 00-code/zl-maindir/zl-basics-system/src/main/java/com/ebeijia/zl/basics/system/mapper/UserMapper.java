package com.ebeijia.zl.basics.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.system.domain.User;

/**
 *
 * 平台用户信息表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public User getUserByName(User user);

    List<User> getUserOmsList(User user);
    
    List<User> getUserDiyList(User user);

    /**
     * 根据手机号查询
     * @param user
     * @return
     */
    User getUserByPhoneNo(User user);
}
