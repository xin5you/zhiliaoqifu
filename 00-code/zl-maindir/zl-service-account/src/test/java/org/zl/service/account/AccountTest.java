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
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class AccountTest {

	@Autowired
	private AccountManageFacade  txnAccountInfFacade;

	/**
	 * 
	* @Description: 供应商开户
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午9:20:54 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
    @Test
   public void createAccountForProvider() throws Exception{
    	AccountOpenReqVo req=new AccountOpenReqVo();
    	req.setTransId(TransCode.MB80.getCode());
    	req.setTransChnl(TransChnl.CHANNEL0.toString());
    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
    	req.setUserChnlId("300000000000000000000000");
    	req.setUserName("zl供应商供应商");
    	req.setUserType(UserType.TYPE300.getCode());
    	req.setCompanyId("300000000000000000000000");
    	req.setDmsRelatedKey(IdUtil.getNextId());
    	
    	
    	Set<String>  bids=new HashSet<>();
    	bids.add(SpecAccountTypeEnum.A00.getbId());
    	bids.add(SpecAccountTypeEnum.A01.getbId());
    	bids.add(SpecAccountTypeEnum.B01.getbId());
    	bids.add(SpecAccountTypeEnum.B02.getbId());
    	
    	req.setbIds(bids);
    	txnAccountInfFacade.createAccount(req);
   }
    
    
    /**
     * 
    * @Description: 知了平台开户
    *
    * @param:描述1描述
    *
    * @version: v1.0.0
    * @author: zhuqi
    * @date: 2018年12月14日 上午9:22:35 
    *
    * Modification History:
    * Date         Author          Version
    *-------------------------------------*
    * 2018年12月14日     zhuqi           v1.0.0
     */
    @Test
   public void createAccountForZL() throws Exception{
    	AccountOpenReqVo req=new AccountOpenReqVo();
    	req.setTransId(TransCode.MB80.getCode());
    	req.setTransChnl(TransChnl.CHANNEL0.toString());
    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
    	req.setUserChnlId("200000000000000000000000");
    	req.setUserName("zl平台");
    	req.setUserType(UserType.TYPE200.getCode());
    	req.setCompanyId("200000000000000000000000");
    	req.setDmsRelatedKey(IdUtil.getNextId());
    	
    	
    	Set<String>  bids=new HashSet<>();
    	bids.add(SpecAccountTypeEnum.A00.getbId());
    	bids.add(SpecAccountTypeEnum.A01.getbId());
    	bids.add(SpecAccountTypeEnum.B01.getbId());
    	bids.add(SpecAccountTypeEnum.B02.getbId());
    	
    	req.setbIds(bids);
    	txnAccountInfFacade.createAccount(req);
   }
	
	
	
	/**
	 * 
	* @Description: 知了企业开户
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午9:23:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
    @Test
   public void createAccountForZLCompany() throws Exception{
    	AccountOpenReqVo req=new AccountOpenReqVo();
    	req.setTransId(TransCode.MB80.getCode());
    	req.setTransChnl(TransChnl.CHANNEL0.toString());
    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
    	req.setUserChnlId("200000000000000000000001");
    	req.setUserName("zl平台企业");
    	req.setUserType(UserType.TYPE200.getCode());
    	req.setCompanyId("200000000000000000000001");
    	req.setDmsRelatedKey(IdUtil.getNextId());
    	
    	Set<String>  bids=new HashSet<>();
    	bids.add(SpecAccountTypeEnum.A00.getbId());
    	bids.add(SpecAccountTypeEnum.A01.getbId());
    	bids.add(SpecAccountTypeEnum.B01.getbId());
    	bids.add(SpecAccountTypeEnum.B02.getbId());
    	
    	req.setbIds(bids);
    	txnAccountInfFacade.createAccount(req);
   }
    
    
	/**
	 * 
	* @Description: 企业员工开户
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 上午9:26:19 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
    @Test
   public void createAccountForUser() throws Exception{
    	AccountOpenReqVo req=new AccountOpenReqVo();
    	req.setTransId(TransCode.CW80.getCode());
    	req.setTransChnl(TransChnl.CHANNEL0.toString());
    	req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
    	req.setUserChnlId(IdUtil.getNextId());
    	req.setMobilePhone("13501755206");
    	req.setUserName("zhuqi");
    	req.setUserType(UserType.TYPE100.getCode());
    	req.setCompanyId("200000000000000000000001");
    	req.setDmsRelatedKey(IdUtil.getNextId());
    	
    	Set<String>  bids=new HashSet<>();
    	bids.add(SpecAccountTypeEnum.A00.getbId());
    	bids.add(SpecAccountTypeEnum.A01.getbId());
    	bids.add(SpecAccountTypeEnum.B01.getbId());
    	bids.add(SpecAccountTypeEnum.B02.getbId());
    	
    	req.setbIds(bids);
    	txnAccountInfFacade.createAccount(req);
   }
}