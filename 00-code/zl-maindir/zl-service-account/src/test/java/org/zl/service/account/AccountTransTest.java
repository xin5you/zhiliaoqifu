package org.zl.service.account;


import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTransTest {

	@Autowired
	private AccountTransactionFacade  accountTransactionFacade;


	/**
	 * 给供应商充值
	 * 
	* @Description: 该函数的功能描述
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午9:41:38 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
//    @Test
//   public void executeRechargeToPrivoder() throws Exception{
//    	AccountRechargeReqVo req=new AccountRechargeReqVo();
//    	
//    	req.setTransId(TransCode.MB20.getCode());
//    	req.setTransChnl(TransChnl.CHANNEL0.toString());
//    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
//    	req.setUserChnlId("300000000000000000000000");
//    	req.setUserType(UserType.TYPE300.getCode());
//    	req.setFromCompanyId("300000000000000000000000");
//    	req.setTransAmt(new BigDecimal(500000));
//    	req.setUploadAmt(new BigDecimal(500000));
//    	req.setDmsRelatedKey(IdUtil.getNextId());
//    	req.setPriBId(SpecAccountTypeEnum.A00.getbId());
//    	
//    	accountTransactionFacade.executeRechargeByOneBId(req);
//   }
	
}