package org.zl.service.account;


import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.core.BatchWithdrawData;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawDetailMapper;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class WithdrawTest2 {

	@Autowired
	private AccountWithdrawDetailMapper accountWithdrawDetailMapper;


	@Test
	public void test2() throws Exception{
		String userId="123";
		long sDate=DateUtil.getStartTimeInMillis();
		long eDate=DateUtil.getEndTimeInMillis();
	 	BigDecimal count=	accountWithdrawDetailMapper.getWithdrawAmtByUserIdAndTime(userId,sDate,eDate);
		System.out.println("count-->"+count);
	}


}