package org.zl.service.account;


import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountQueryTest {

	@Autowired
	private IAccountInfService accountInfService;


	/**
	 * 查询供应商账户信息
	 * @throws Exception
	 */
/*    @Test
   public void getAccountInfListTo300() throws Exception{
		AccountQueryReqVo req = new AccountQueryReqVo();
		req.setUserType(UserType.TYPE300.getCode());
		req.setUserChnlId("30b2cc3b-ff9a-4da8-82a7-eb281e0fea9d");
		req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		List<AccountVO> list=accountInfService.getAccountInfList(req);
		System.out.println(JSONArray.toJSONString(list));
	}*/

	/**
	 * 查询企业账户信息
	 * @throws Exception
	 */
/*	@Test
	public void getAccountInfListTo200() throws Exception{
		AccountQueryReqVo req = new AccountQueryReqVo();
		req.setUserType(UserType.TYPE200.getCode());
		req.setUserChnlId("01935ab7-ee9d-4799-b90f-c1501c242fe5");
		req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		List<AccountVO> list=accountInfService.getAccountInfList(req);
		System.out.println(JSONArray.toJSONString(list));
	}*/

	/**
	 * 查询分销商账户信息
	 * @throws Exception
	 */
	@Test
	public void getAccountInfListTo400() throws Exception{
		AccountQueryReqVo req = new AccountQueryReqVo();
		req.setUserType(UserType.TYPE200.getCode());
		req.setUserChnlId("c524deaa-f669-44ff-85d2-4cc5f5d296e7");
		req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
		List<AccountVO> list=accountInfService.getAccountInfList(req);
		System.out.println(JSONArray.toJSONString(list));
	}


}