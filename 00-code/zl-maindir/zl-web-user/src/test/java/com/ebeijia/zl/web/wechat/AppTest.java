package com.ebeijia.zl.web.wechat;

import com.ebeijia.zl.UserApp;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.SmsVo;
import com.ebeijia.zl.common.utils.enums.SMSType;
import com.ebeijia.zl.core.activemq.service.MQProducerService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
public class AppTest {
    @Autowired
    private MQProducerService mqProducerService;

    @org.junit.Test
    public void sendSMS() throws Exception{
        SmsVo vo=new SmsVo();
        vo.setMsgId(IdUtil.getNextId());
        vo.setSmsType(SMSType.SMSType1000.getCode());
        vo.setPhoneNumber("13501755206");
        vo.setCode("123459");
        mqProducerService.sendSMS(vo);
    }
}
