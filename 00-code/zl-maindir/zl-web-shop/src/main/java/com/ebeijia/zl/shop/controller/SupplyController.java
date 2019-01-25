package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.service.supply.ISupplyService;
import com.ebeijia.zl.shop.utils.RequestJsonUtil;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    private ISupplyService supplyService;

    @Autowired
    private ShopUtils shopUtils;

    private static Logger logger = LoggerFactory.getLogger(SupplyController.class);


    //直充接口
    @TokenCheck(force = true)
    @ApiOperation("手机充值接口")
    @RequestMapping(value = "/phone/charge", method = RequestMethod.POST)
    public JsonResult<String> phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session) {
        String result;

        if ("A02".equals(payInfo.getTypeA())) {
            result = supplyService.outerPayPhoneCharge(phone, amount, validCode, payInfo, session);
        } else {
            result = supplyService.phoneCharge(phone, amount, validCode, payInfo, session);
        }
        return new JsonResult<>(result);
    }

    //直充接口
    @ApiOperation("直充回调接口")
    @RequestMapping(value = "/phone/charge/callback", method = RequestMethod.POST)
    public String phoneChargeCallback(HttpServletRequest request) {
        BaseResult respVO = null;
        try {
            String requestJsonString = RequestJsonUtil.getRequestJsonString(request);
            logger.info("bmHKbCallBack getRequestJsonString-->{}", requestJsonString);
            respVO = shopUtils.readValue(requestJsonString, BaseResult.class);
        } catch (IOException e) {
            logger.error("回调入参[{}]", e);
        }
        LinkedHashMap<String,String> map = (LinkedHashMap) respVO.getObject();
        Integer i = supplyService.phoneChargeCallback(map);
        if (ResultState.OK == i) {
            return "SUCCESS";
        }
        return "ERROR";
    }


}
