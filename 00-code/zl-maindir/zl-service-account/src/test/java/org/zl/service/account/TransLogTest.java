package org.zl.service.account;


import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.user.req.OpenUserInfReqVo;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import com.ebeijia.zl.service.account.mapper.TransLogMapper;
import com.ebeijia.zl.service.account.service.IAccountLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class TransLogTest {

	@Autowired
	private IAccountLogService accountLogService;

    @Test
    public void getAccountLogVoList(){
    	
    	AccountQueryReqVo req=new AccountQueryReqVo();
/*    	req.setUserType(UserType.TYPE100.getCode());
    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
    	req.setUserChnlId("cdeb7a4a-859a-4d23-b3c6-03782a51688d");*/
    	req.setActPrimaryKey("02354413-2b31-4729-a501-2e153026c16e");
/*		req.setSDate(1545103378868l);
		req.setEDate(System.currentTimeMillis());*/
		List<AccountLogVO> list=accountLogService.getAccountLogVoList(req);

		System.out.println(JSONArray.toJSONString(list));
    }
    
  
}