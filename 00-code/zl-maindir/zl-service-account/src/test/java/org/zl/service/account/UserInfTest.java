package org.zl.service.account;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.facade.user.req.OpenUserInfReqVo;
import com.ebeijia.zl.facade.user.service.UserInfFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class UserInfTest {

	@Autowired
	private UserInfFacade userInfFacade;

    @Test
    public void getUserRegister(){
    	
    	OpenUserInfReqVo req=new OpenUserInfReqVo();
    	
    	req.setTransId(TransCode.CW80.getCode());
    	req.setTransChnl("40001001");
    	req.setMobilePhone("13501755206");
    	req.setUserName("zhuqi");
    	req.setUserType(UserType.TYPE100.getCode());
    	req.setCompanyId("100000000000000000000000");
    	req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
    	req.setUserChnlId(IdUtil.getNextId());


//    	req.setCardType(UserType.TYPE100.getCode());

    	userInfFacade.registerUserInf(req);

    }
    
  
}