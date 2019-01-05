package com.ebeijia.zl.shop.service.pay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomPayCard;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomPayCardService;
import com.ebeijia.zl.shop.service.pay.ICardService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.CardBindInfo;
import com.ebeijia.zl.shop.vo.CardInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@Service
public class CardService implements ICardService {
    private static String bankKv = "{\"BOC\":\"中国银行\",\"ICBC\":\"工商银行\",\"CCB\":\"建设银行\",\"COMM\":\"交通银行\",\"HXB\":\"华夏银行\",\"CMB\":\"招商银行\",\"CEB\":\"光大银行\",\"ABC\":\"农业银行\",\"CMBC\":\"民生银行\",\"CIB\":\"兴业银行\",\"CITIC\":\"中信银行\",\"BJB\":\"北京银行\",\"aSPDB\":\"浦发银行\",\"BOSH\":\"上海银行\",\"PAB\":\"平安银行\",\"PSBC\":\"邮政储蓄银行\",\"EGB\":\"恒丰银行\",\"CZB\":\"浙商银行\",\"CBHB\":\"渤海银行\",\"XTB\":\"邢台银行\",\"ZJCCB\":\"张家口市商业银行\",\"BOCD\":\"承德银行\",\"BOCZ\":\"沧州银行\",\"BOJS\":\"晋商银行\",\"JCB\":\"晋城市商业银行\",\"BOIM\":\"内蒙古银行\",\"BSB\":\"包商银行\",\"ORDOSB\":\"鄂尔多斯银行\",\"DLB\":\"大连银行\",\"BOAS\":\"鞍山市商业银行\",\"BOJZ\":\"锦州银行\",\"BOHL\":\"葫芦岛银行\",\"BOYK\":\"营口银行\",\"BOFX\":\"阜新银行\",\"BOJL\":\"吉林银行\",\"HRBCB\":\"哈尔滨银行\",\"LJB\":\"龙江银行\",\"NJCB\":\"南京银行\",\"JSBC\":\"江苏银行\",\"HZCB\":\"杭州银行\",\"NBCB\":\"宁波银行\",\"WZCB\":\"温州银行\",\"BOHZ\":\"湖州银行\",\"BOSX\":\"绍兴银行营业部\",\"CZCB\":\"浙江稠州商业银行\",\"TZB\":\"台州银行\",\"ZJTLCB\":\"浙江泰隆商业银行\",\"MTCB\":\"浙江民泰商业银行\",\"FHB\":\"福建海峡银行\",\"XMCCB\":\"厦门银行\",\"NCB\":\"南昌银行\",\"GZBANK\":\"赣州银行\",\"SRB\":\"上饶银行\",\"QDCCB\":\"青岛银行\",\"QSB\":\"齐商银行\",\"DYCCB\":\"东营市商业银行\",\"YTB\":\"烟台银行\",\"BOWF\":\"潍坊银行\",\"BOJN\":\"济宁银行\",\"TACCB\":\"泰安市商业银行\",\"LSB\":\"莱商银行\",\"WHCCB\":\"威海市商业银行\",\"DZB\":\"德州银行\",\"BOLS\":\"临商银行\",\"BORZ\":\"日照银行\",\"BOZZ\":\"郑州银行\",\"CBOK\":\"开封市商业银行\",\"BOLY\":\"洛阳银行\",\"BOLH\":\"漯河市商业银行\",\"SQB\":\"商丘市商业银行\",\"BNY\":\"南阳市商业银行\",\"HKB\":\"汉口银行\",\"CSCB\":\"长沙银行\",\"GZB\":\"广州银行\",\"SZDB\":\"深发展银行\",\"HKBEA\":\"东莞银行\",\"GBGB\":\"广西北部湾银行\",\"BOLZ\":\"柳州银行\",\"CQCB\":\"重庆银行\",\"PCCB\":\"攀枝花市商业银行\",\"BODY\":\"德阳银行\",\"MYCCB\":\"绵阳市商业银行\",\"GYCB\":\"贵阳市商业银行\",\"FDB\":\"富滇银行\",\"LZYH\":\"兰州银行\",\"BOQH\":\"青海银行营业部\",\"BONX\":\"宁夏银行\",\"UCCB\":\"乌鲁木齐市商业银行\",\"BOK\":\"昆仑银行\",\"BOSZ\":\"苏州银行\",\"KSNX\":\"昆山农村商业银行\",\"WJRCB\":\"吴江农村商业银行\",\"CSRCB\":\"江苏常熟农村商业银行\",\"RCBOZ\":\"张家港农村商业银行\",\"GRCB\":\"广州农村商业银行\",\"SDEB\":\"佛山顺德农村商业银行\",\"CQRCB\":\"重庆农村商业银行\",\"CGB\":\"广发银行\",\"TJRCB\":\"天津农村合作银行\",\"TCCB\":\"天津银行\",\"HSB\":\"徽商银行\",\"SRCB\":\"上海农村商业银行\",\"BJRCB\":\"北京农村商业银行\",\"JSRCU\":\"江苏省农村信用社\",\"BEEB\":\"宁波鄞州农村合作银行\",\"ARCU\":\"安徽省农村信用联社\",\"FJNX\":\"福建省农村信用社\",\"HBNX\":\"湖北省农村信用社\",\"SZRCB\":\"深圳农村商业银行\",\"BODG\":\"东莞农村商业银行\",\"GXNX\":\"广西壮族自治区农村信用社\",\"HNNX\":\"海南省农村信用社\",\"YNRCC\":\"云南省农村信用社\",\"YRRCB\":\"宁夏黄河农村商业银行\",\"KEB\":\"外换银行\",\"XHB\":\"新韩银行\",\"IBK\":\"企业银行\",\"HYB\":\"韩亚银行\",\"HEBB\":\"河北银行\",\"BOHD\":\"邯郸市商业银行\",\"CRBZ\":\"珠海华润银行股份有限公司清算中心\",\"ZGCCB\":\"自贡市商业银行清算中心\",\"GNB\":\"广东南粤银行股份有限公司\",\"GLB\":\"桂林银行股份有限公司\",\"JLRCC\":\"吉林省农村信用社联合社（不办理转汇业务）\",\"JXB\":\"嘉兴银行股份有限公司清算中心(不对外办理业务）\",\"LFB\":\"廊坊银行\",\"SDRCC\":\"山东省农村信用社联合社(不对外办理业务)\",\"WBC\":\"友利银行(中国)有限公司\",\"CAB\":\"长安银行股份有限公司\",\"ZJRCC\":\"浙江省农村信用社联合社\",\"PDSB\":\"平顶山银行股份有限公司\",\"BSYZB\":\"北京顺义银座村镇银行股份有限公司\",\"GDHXB\":\"广东华兴银行股份有限公司\",\"JGYZB\":\"江西赣州银座村镇银行股份有限公司\",\"QLB\":\"齐鲁银行\",\"SFYZB\":\"深圳福田银座村镇银行股份有限公司\",\"ZJYZB\":\"浙江景宁银座村镇银行股份有限公司\",\"ZSYZB\":\"浙江三门银座村镇银行股份有限公司\",\"CQYZB\":\"重庆黔江银座村镇银行股份有限公司\",\"CYYZB\":\"重庆渝北银座村镇银行股份有限公司\",\"CDRCB\":\"成都农商银行\",\"NCCCB\":\"南充市商业银行股份有限公司\",\"BOCD1\":\"成都银行股份有限公司\",\"SJB\":\"盛京银行\",\"QTB\":\"其他银行\"}";
    private static HashMap<String, String> bankMap = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(CardService.class);

    static {
        try {
            bankMap = new ObjectMapper().readValue(bankKv, bankMap.getClass());
            bankKv = null;
            logger.info(String.format("成功初始化了%s个银行简称",bankMap.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private ShopUtils shopUtils;

    @Autowired
    private ITbEcomPayCardService cardDao;

    @Override
    public CardInfo validCardNum(String cardNum) {
        RestTemplate restT = new RestTemplate();
        //通过Jackson JSON processing library直接将返回值绑定到对象
        CardInfo cardInfo = restT.getForObject("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=" + cardNum + "&cardBinCheck=true", CardInfo.class);
        if (cardInfo == null) {
            logger.error("网络通讯异常%s", "[validCardNum]");
            throw new BizException(500, "通讯异常");
        }
        String bankName = bankMap.get(cardInfo.getBank());
        if (bankName == null) {
            throw new AdviceMessenger(406, "请尝试使用其他银行卡");
        }
        cardInfo.setBankName(bankName);
        return cardInfo;
    }

    @Override
    public TbEcomPayCard listAccountCard() {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(406, "参数异常");
        }
        String memberId = memberInfo.getMemberId();

        TbEcomPayCard temp = new TbEcomPayCard();
        temp.setMemberId(memberId);
        //检查是否存在卡记录
        TbEcomPayCard card = cardDao.getOne(new QueryWrapper<>(temp));
        if (card == null) {
            throw new AdviceMessenger(ResultState.OK, "您还未绑卡");
        }
        return card;
    }

    @Override
    public Integer bindCard(CardBindInfo card) {
        if (StringUtils.isAnyEmpty(card.getBankDetail(),card.getUserName(),card.getCardNumber(),card.getIdCard())){
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE,"缺少参数");
        }
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(406, "参数异常");
        }
        String memberId = memberInfo.getMemberId();

        CardInfo cardInfo = validCardNum(card.getCardNumber());

        TbEcomPayCard temp = new TbEcomPayCard();
        //检查是否有相同卡信息
        temp.setCardNumber(card.getCardNumber());
        if (cardDao.getOne(new QueryWrapper<>(temp))!=null){
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE,"您输入的卡已被绑定");
        }
        temp.setCardNumber(null);
        //检查用户是否存在记录
        temp.setMemberId(memberId);
        TbEcomPayCard one = cardDao.getOne(new QueryWrapper<>(temp));
        temp.setBankCode(cardInfo.getBank());
        temp.setUserName(card.getUserName());
        temp.setBankDetail(card.getBankDetail());
        temp.setCardNumber(card.getCardNumber());
        temp.setProvince(card.getProvince());
        temp.setCity(card.getCity());
        temp.setRegion(card.getCounty());
        temp.setIdCard(card.getIdCard());
        temp.setBankName(cardInfo.getBankName());
        //如果没有则添加
        if (one == null) {
            one = temp;
            one.setCardId(IdUtil.getNextId());
            boolean save = cardDao.save(one);
            if (!save) {
                throw new AdviceMessenger(500, "数据插入失败，请重试");
            }
        } else {
            boolean update = cardDao.update(temp, new QueryWrapper<>(one));
            if (!update) {
                throw new AdviceMessenger(500, "数据插入失败，请重试");
            }
        }
        return 200;
    }


}
