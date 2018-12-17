package com.ebeijia.zl.web.oms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.ebeijia.zl.web.oms.sys.mapper.UserMapper;
import com.ebeijia.zl.web.oms.sys.model.UserRole;
import com.ebeijia.zl.web.oms.sys.service.UserRoleService;
import com.ebeijia.zl.web.oms.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 平台用户信息表 Service 实现类
 *
 * @User myGen
 * @Date 2018-12-17
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据登录名得到用户对象
     * @param loginName
     * @return User
     */
    public User getUserByName(String userName, String loginName, String loginType){
        User user = new User();
        user.setUserName(userName);
        user.setLoginName(loginName);
        user.setLoginType(loginType);
        return userMapper.getUserByName(user);
    }





    public int updateUser(User user, String [] rolesId)throws Exception {
        int operNum=this.updateById(user) ? 1:0;
        this.deleteUserRoleByUserId(user.getId().toString());

        if(rolesId !=null && rolesId.length>0){
            UserRole userRole=null;
            for(int i=0;i<rolesId.length;i++){
                userRole=new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(rolesId[i]);
                userRoleService.save(userRole);
            }
        }
        return operNum;
    }


    public int deleteUser(String userId) throws Exception{
        User user=this.getById(userId);
        user.setDataStat("1");
        return this.updateById(user) ? 1:0;
    }

    /**
     * 添加用户
     * @param user
     * @param rolesId
     * @return
     * @throws Exception
     */
    public int saveUser(User user, String [] rolesId) throws Exception {
        int operNum = this.save(user) ?1:0;

        if(rolesId != null && rolesId.length > 0){
            UserRole userRole = null;
            for(int i=0; i<rolesId.length; i++){
                userRole=new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(rolesId[i]);
                userRoleService.save(userRole);
            }
        }
        return operNum;
    }
    /**
     * 用户列表
     * @param startNum
     * @param pageSize
     * @param user
     * @return
     */
    public PageInfo<User> getUserPage(int startNum, int pageSize, User user){
        PageHelper.startPage(startNum, pageSize);
        List<User> userList = userMapper.getUserList(user);
        for (User diyUser : userList) {
            if ("分销商管理员".equals(diyUser.getRoleName())) {
                diyUser.setRoleCheckflag("0");
            } else {
                diyUser.setRoleCheckflag("1");
            }
        }
        PageInfo<User> userPage = new PageInfo<User>(userList);
        return userPage;
    }

    /**
     * 增加用户角色
     * @param entity
     */
    public void saveUserRole(UserRole entity) {
        userRoleService.save(entity);
    }


    /**
     * 删除用户角色
     * @param userId
     */
    public void deleteUserRoleByUserId(String userId) {
        userMapper.deleteUserRoleByUserId(userId);

    }

    @Override
    public PageInfo<User> getDiyUserPage(int startNum, int pageSize, User user) {
        PageHelper.startPage(startNum, pageSize);
        List<User> userList = userMapper.getUserList(user);
        PageInfo<User> userPage = new PageInfo<User>(userList);
        return userPage;
    }

    @Override
    public User getUserByPhoneNo(User user) {
        return userMapper.getUserByPhoneNo(user);
    }
}
