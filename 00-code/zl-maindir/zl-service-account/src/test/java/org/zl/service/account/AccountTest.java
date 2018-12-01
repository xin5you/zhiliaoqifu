package org.zl.service.account;


import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.service.IUserInfService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTest {

    @Autowired
    private IUserInfService userInfService;

    @Test
    public void getUser(){
    	UserInf user=new UserInf();
    	user.setUserId(UUID.randomUUID().toString());
    	user.setDataStat("1");
    	user.setCreateUser("123");
    	user.setCreateTime(System.currentTimeMillis());
    	user.setLockVersion(1);
    	userInfService.saveOrUpdate(user);
    	
    	//条件查询 按顺序执行
//    	QueryWrapper<UserInf> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_name", "3");
//        queryWrapper.eq("data_stat", "1");
//        UserInf result = userInfService.getOne(queryWrapper);
//    		System.out.println(result);


    }
}