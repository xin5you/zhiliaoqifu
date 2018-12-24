package com.ebeijia.zl.basics.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.system.domain.User;
import com.github.pagehelper.PageInfo;


/**
 *
 * 平台用户信息表 Service 接口类
 *
 * @User myGen
 * @Date 2018-12-17
 */
public interface UserService extends IService<User> {

    User getUserByName(String userName, String loginName, String loginType);

    /**
     * 用户列表
     * @param startNum
     * @param pageSize
     * @param user
     * @return
     */
    public PageInfo<User> getUserPage(int startNum, int pageSize, User user);

    /**
     * 根据手机号查询用户信息
     * @param user
     * @return
     */
    User getUserByPhoneNo(User user);

    User getUserByOrganId(User user);
}
