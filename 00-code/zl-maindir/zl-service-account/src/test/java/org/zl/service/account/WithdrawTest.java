package org.zl.service.account;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.core.BatchWithdrawData;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawBodyVO;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawDetailDataVO;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountWithDrawReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
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

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

    @Test
   public void toWithDraw() throws Exception{

        AccountWithDrawReqVo reqVo=new AccountWithDrawReqVo();
        String dmsRelateKey=IdUtil.getNextId();
         reqVo.setTransId(TransCode.CW91.getCode());
         reqVo.setTransChnl(TransChnl.CHANNEL5.toString());
         reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
         reqVo.setUserChnlId("7fedc0ae-0d0f-4ff4-9209-a0c0d448821f");
         reqVo.setUserType(UserType.TYPE100.getCode());
         reqVo.setDmsRelatedKey(dmsRelateKey);

         reqVo.setReceiverCardNo("6214830215284406"); //收款卡号
         reqVo.setReceiverName("朱秋友");
         reqVo.setBankName("招商银行");  //开户行
         reqVo.setBankCode("CMB");  //开户行编号
         reqVo.setTransAmt(new BigDecimal(1));
         reqVo.setUploadAmt(new BigDecimal(1));
         reqVo.setReceiverType("PERSON");
         reqVo.setRemarks(null);
         reqVo.setOrderName(null);

        BaseResult result= accountTransactionFacade.executeWithDraw(reqVo);
        System.out.println("dmsRelateKey-->"+dmsRelateKey);
        System.out.println(JSONArray.toJSONString(result));

   }
}