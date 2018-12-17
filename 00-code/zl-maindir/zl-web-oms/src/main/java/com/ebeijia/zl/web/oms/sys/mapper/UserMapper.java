package com.ebeijia.zl.web.oms.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.web.oms.sys.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    List<User> getUserList(User user);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteUserRoleByUserId(String userId);

    /**
     * 根据手机号查询
     * @param user
     * @return
     */
    User getUserByPhoneNo(User user);
}
