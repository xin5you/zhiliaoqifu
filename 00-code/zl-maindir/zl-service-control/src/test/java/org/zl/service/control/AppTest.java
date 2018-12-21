package org.zl.service.control;


import java.util.HashSet;
import java.util.Set;

import com.ebeijia.zl.ControlApp;
import com.ebeijia.zl.common.utils.domain.SmsVo;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.core.activemq.service.MQProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ControlApp.class)//这里的Application是springboot的启动类名
public class AppTest {

    @Autowired
    private MQProducerService mqProducerService;

    @Test
    public void sendSMS() throws Exception{
        SmsVo vo=new SmsVo();
        vo.setMsgId(IdUtil.getNextId());
        vo.setSmsType(SMSType.SMSType1000.getCode());
        vo.setPhoneNumber("13501755206");
        vo.setCode("123458");
        mqProducerService.sendSMS(vo);
    }
}