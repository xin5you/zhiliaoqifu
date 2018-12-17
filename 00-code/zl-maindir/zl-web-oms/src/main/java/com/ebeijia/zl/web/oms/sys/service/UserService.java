package com.ebeijia.zl.web.oms.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.model.UserRole;
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
     * 保存用户信息
     * @param user
     * @param rolesIds
     * @return
     */
    int saveUser(User user,String [] rolesIds) throws Exception;

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    int updateUser(User user,String [] rolesId) throws Exception;
    /**
     * 用户列表
     * @param startNum
     * @param pageSize
     * @param user
     * @return
     */
    public PageInfo<User> getUserPage(int startNum, int pageSize, User user);

    /**
     * 增加用户角色
     * @param entity
     */
    void saveUserRole(UserRole entity);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteUserRoleByUserId(String userId);

    /**
     * 用户列表（商户自助服务）
     *
     * @param startNum
     * @param pageSize
     * @param user
     * @return
     */
    public PageInfo<User> getDiyUserPage(int startNum, int pageSize, User user);

    /**
     * 根据手机号查询用户信息
     * @param user
     * @return
     */
    User getUserByPhoneNo(User user);
}
