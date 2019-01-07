package org.zl.service.account;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.core.BatchWithdrawData;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawBodyVO;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawDetailDataVO;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountWithDrawReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class WithdrawTest {

	@Autowired
	private YFBWithdrawConfig yfbWithdrawConfig;

     @Autowired
	private BatchWithdrawData batchWithdrawData;


    @Test
   public void testConfig() throws Exception{

         AccountWithDrawReqVo reqVo=new AccountWithDrawReqVo();

         reqVo.setDmsRelatedKey(IdUtil.getNextId());

         reqVo.setReceiverCardNo("6214830215284406"); //收款卡号
         reqVo.setReceiverName("朱秋友");
         reqVo.setBankName("招商银行");  //开户行
         reqVo.setBankCode("CMB");  //开户行编号
         reqVo.setTransAmt(new BigDecimal(1));
         reqVo.setUploadAmt(new BigDecimal(1));
         reqVo.setReceiverType("PERSON");
         reqVo.setRemarks(null);
         reqVo.setOrderName(null);


   }
}