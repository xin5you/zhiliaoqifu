package org.zl.service.account;


import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTest {

	@Autowired
	private AccountManageFacade  txnAccountInfFacade;


    @Test
   public void createAccount() throws Exception{
    	AccountOpenReqVo req=new AccountOpenReqVo();
    	
    	req.setTransId(TransCode.MB80.getCode());
    	req.setTransChnl("40001001");
//    	req.setMobilePhone("13501755206");
    	req.setUserName("ebj供应商");
    	req.setUserType(UserType.TYPE300.getCode());
    	req.setCompanyId("100000000000000000000000");
    	req.setDmsRelatedKey(IdUtil.getNextId());
    	
    	
    	Set<String>  bids=new HashSet<>();
    	bids.add("A0");
//    	bids.add("A1");
    	bids.add("B1");
//    	bids.add("B2");
//    	bids.add("B3");
//    	bids.add("B4");
//    	bids.add("B5");
//    	bids.add("B6");
//    	bids.add("B7");
    	
    	req.setbIds(bids);
    	txnAccountInfFacade.createAccount(req);
   }
}