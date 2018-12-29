package org.zl.service.account;


import com.ebeijia.zl.AccountApp;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.core.BatchWithdrawData;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)//这里的Application是springboot的启动类名
public class WithdrawTest2 {

	@Autowired
	private IAccountWithdrawOrderService accountWithdrawOrderService;




}