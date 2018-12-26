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
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

     /*   WithdrawDetailDataVO vo=new WithdrawDetailDataVO();
        vo.setSerialNo(UUID.randomUUID().toString().replace("-",""));
        vo.setReceiverCardNo("6214830215284406"); //收款卡号
        vo.setReceiverName("朱秋友");
        vo.setBankName("招商银行");  //开户行
        vo.setBankCode("CMB");  //开户行编号
        vo.setAmount(1l);
        vo.setRemark(null);
        vo.setOrderName(null);

        List<WithdrawDetailDataVO> detailData=new ArrayList<WithdrawDetailDataVO>();
        detailData.add(vo);

        WithdrawBodyVO body=new WithdrawBodyVO();
        body.setDetailData(detailData);

        body.setBatchNo(UUID.randomUUID().toString().replace("-",""));
        body.setMerchantNo(yfbWithdrawConfig.getMerchantNo());
        body.setProductCode(yfbWithdrawConfig.getProductCode());
        body.setTotalNum(1);
        body.setTotalAmount(1l);
        body.setPayDate(DateUtil.getCurrentDateStr());
        body.setNotifyUrl("http://zlqfwebapi.free.idcfengye.com/web-api/api/withdraw/suning-yfb/notify");

        WithdrawContentVO content=new WithdrawContentVO();
        content.setBody(body);
        content.setPublicKeyIndex(yfbWithdrawConfig.getPublicKeyIndex());
        content.setMerchantNo(yfbWithdrawConfig.getMerchantNo());*/

         String s=batchWithdrawData.batchWithDraw(null);

         System.out.println("batchWithDraw->"+s);

   }
}