package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomPayCard;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import com.ebeijia.zl.shop.service.pay.ICardService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.service.valid.impl.ValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(value = "/pay", description = "用于定义支付、信用卡相关接口")
@RequestMapping(value = "/pay")
@RestController
public class PayController {
    @Autowired
    private IPayService payService;

    @Autowired
    private ICardService cardService;

    @Autowired
    ValidCodeService validCodeService;

    @Autowired
    ShopUtils shopUtils;

    @TokenCheck(force = true)
    @ApiOperation("绑定银行卡")
    @RequestMapping(value = "/card/bind", method = RequestMethod.POST)
    public JsonResult<Integer> bindBankCard(CardBindInfo card,String validCode) {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(406, "参数异常");
        }
        boolean valid = validCodeService.checkValidCode(PhoneValidMethod.PAY, memberInfo.getMobilePhoneNo(), validCode);
        if (!valid) {
            throw new AdviceMessenger(403, "验证码有误");
        }
        return new JsonResult<>(cardService.bindCard(card));
    }


    @TokenCheck(force = true)
    @ApiOperation("校验银行卡号")
    @RequestMapping(value = "/card/valid", method = RequestMethod.POST)
    public JsonResult<CardInfo> validBankCard(@RequestParam("cardnum") String cardNum) {
        CardInfo cardInfo = cardService.validCardNum(cardNum);
        return new JsonResult<>(cardInfo);
    }

    @TokenCheck(force = true)
    @ApiOperation("列出银行卡")
    @RequestMapping(value = "/card/list", method = RequestMethod.GET)
    public JsonResult<TbEcomPayCard> listAccountCard() {
        TbEcomPayCard cardInfo = cardService.listAccountCard();
        return new JsonResult<>(cardInfo);
    }

    //支付接口
//    @TokenCheck(force = true)
//    @ApiOperation("支付订单")
//    @RequestMapping(value = "/deal/order/{orderid}", method = RequestMethod.POST)
//    public void payOrder(@PathVariable("orderid") String orderId, PayInfo payInfo, @RequestParam("session") String session) {
//        payService.payOrder(payInfo, session);
//    }


    @ApiOperation("列出所有专项账户类型的ID")
    @RequestMapping(value = "/billingtype/list", method = RequestMethod.GET)
    public JsonResult<List<SpecAccountTypeEnum>> listBillingType() {
        List<SpecAccountTypeEnum> list = Arrays.asList(SpecAccountTypeEnum.values());
        return new JsonResult<>(list);
    }


    @TokenCheck(force = true)
    @ApiOperation("列出可用专项账户类型与余额")
    @RequestMapping(value = "/balance/list/", method = RequestMethod.GET)
    public JsonResult<List<AccountVO>> listAccountDetail(@RequestParam("session") String session) {
        MemberInfo memberInfo = shopUtils.getSession();
        List<AccountVO> accountVOS = payService.listAccountDetail(memberInfo.getOpenId(), session);
        return new JsonResult<>(accountVOS);
    }

    //交易流水
    @TokenCheck(force = true)
    @ApiOperation("列出交易流水记录")
    @RequestMapping(value = "/deal/list/{type}", method = RequestMethod.GET)
    public JsonResult<PageInfo<AccountLogVO>> listAccountDeals(@PathVariable("type") String type,@RequestParam(value = "range",required = false) String range ,@RequestParam(value = "start", required = false) String start, @RequestParam(value = "limit", required = false) String limit, @RequestParam String session) {

        PageInfo<AccountLogVO> deals = payService.listDeals(range, type, start, limit);
        return new JsonResult<>(deals);
    }

    //交易流水
    @TokenCheck(force = true)
    @ApiOperation("列出交易流水记录")
    @RequestMapping(value = "/deal/list", method = RequestMethod.GET)
    public JsonResult<PageInfo<AccountLogVO>> listAccountDealst(@RequestParam(value = "range",required = false) String range ,@RequestParam(value = "start", required = false) String start, @RequestParam(value = "limit", required = false) String limit, @RequestParam String session) {
        PageInfo<AccountLogVO> deals = payService.listDeals(range, null, start, limit);
        return new JsonResult<>(deals);
    }


    //交易流水
    @TokenCheck(force = true)
    @ApiOperation("获取订单DMS对应的流水记录")
    @RequestMapping(value = "/deal/get/{dms}", method = RequestMethod.GET)
    public JsonResult<List<TbEcomPayOrderDetails>> getDealInfoByDms(@PathVariable("dms") String dms) {
        List<TbEcomPayOrderDetails> deals = payService.getDeal(dms);
        return new JsonResult<>(deals);
    }

    /**
     * 托管账户转账
     *
     * @return
     */
    @TokenCheck
    @ApiOperation("托管账户转出到银行卡")
    @ApiResponses({@ApiResponse(code = ResultState.OK, message = "操作成功"),
            @ApiResponse(code = ResultState.ERROR, message = "服务器内部异常"),
            @ApiResponse(code = ResultState.FORBIDDEN, message = "权限不足"),
            @ApiResponse(code = ResultState.UNAUTHORIZED, message = "请重新登录")})
    @RequestMapping(value = "/deal/transfer", method = RequestMethod.POST)
    public JsonResult transferToCard(@Param("deal") DealInfo dealInfo, @Param(value = "session") Double session) {
        if (session == null) {
            session = 0D;
        }
        int state = payService.transferToCard(dealInfo, session);
        return new JsonResult(state);
    }

}
