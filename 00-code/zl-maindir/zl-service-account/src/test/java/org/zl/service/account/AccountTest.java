package org.zl.service.account;


import java.util.UUID;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    	user.setDataStat("0");
    	user.setCreateUser("2");
    	user.setCreateTime(System.currentTimeMillis());
    	user.setLockVersion(1);
    	
    	
//    	QueryWrapper<UserInf> query=new QueryWrapper(user);
//    	UserInf result = userInfService.getOne(query);
//    	System.out.println(result);
    	userInfService.save(user);
    }
}