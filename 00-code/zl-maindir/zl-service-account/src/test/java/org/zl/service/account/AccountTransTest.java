package org.zl.service.account;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTransTest {

	@Autowired
	private AccountTransactionFacade  accountTransactionFacade;


    @Test
   public void executeRechargeByOneBId() throws Exception{
//    	AccountTransactionReq req=new AccountTransactionReq();
    	
//    	req.setTransId(TransCode.MB20.getCode());
//    	req.setTransChnl("40001001");
//    	req.setUserType(UserType.TYPE300.getCode());
//    	req.setToCompanyId("100000000000000000000000");
//    	req.setTransAmt(new BigDecimal(10000));
//    	req.setUploadAmt(new BigDecimal(10000));
//    	req.setDmsRelatedKey(IdUtil.getNextId());
//    	req.setTfrInBId(SpecAccountTypeEnum.A0.getbId());
    	
//    	accountTransactionFacade.executeRechargeByOneBId(req);
   }
}