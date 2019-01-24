package org.zl.service.account;


import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.service.user.service.IPersonInfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class PersonInfServiceTest {

	@Autowired
	private IPersonInfService personInfService;

    @Test
   public void getPersonInfByPhoneNo() throws Exception{
		PersonInf personInf=personInfService.getPersonInfByPhoneNo("13501755206");
		System.out.println(JSONArray.toJSONString(personInf));
   }
}