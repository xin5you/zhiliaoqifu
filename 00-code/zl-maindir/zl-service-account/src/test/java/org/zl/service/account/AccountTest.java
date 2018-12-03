package org.zl.service.account;


import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.user.mapper.PersonInfMapper;
import com.ebeijia.zl.service.user.mapper.UserInfMapper;
import com.ebeijia.zl.service.user.service.IUserInfService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTest {

    @Autowired
    private PersonInfMapper personInfMapper;
    
    @Autowired
    private UserInfMapper userInfMapper;
    
    
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
    
    @Test
    public void getUser1(){
//    	PersonInf personInf=personInfMapper.getPersonInfByPhoneNo("13501755206", "400010001");
    
//    	PersonInf personInf=personInfMapper.selectById("111");
    	
    	UserInf userInf=userInfMapper.getUserInfByExternalId("13501755206", "400010001");
    	
    }
}