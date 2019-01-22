package com.ebeijia.zl.shop.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.core.redis.utils.JedisUtilsWithNamespace;
import com.ebeijia.zl.facade.user.req.OpenUserInfReqVo;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMemberAddress;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomPayCard;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberAddressService;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomPayCardService;
import com.ebeijia.zl.shop.service.auth.IAuthService;
import com.ebeijia.zl.shop.service.valid.IValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;

@Service
public class AuthService implements IAuthService {
    @Value("${weixin.api.code:http://user.happy8888.com.cn/web-user/w/getOpenId}")
    private String wxCodeApi;

    @Value("${weixin.api.userinfo:http://user.happy8888.com.cn/web-user/w/getOpenId}")
    private String wxUserInfoApi;

    private static Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private JedisClusterUtils jedis;

    @Autowired
    private JedisUtilsWithNamespace jedis2;

    @Autowired
    private IValidCodeService validCodeService;

    @Autowired
    private ITbEcomMemberService memberDao;

    @Autowired
    private UserInfFacade userInfFacade;

    @Autowired
    private ITbEcomMemberAddressService addressDao;

    @Autowired
    private ITbEcomPayCardService cardDao;

    @Autowired
    private ShopUtils shopUtils;

    @Override
    @ShopTransactional
    public Token phoneLogin(String phone, String pwd, String code) {
        String openId = null;
        if (!StringUtils.isEmpty(code)) {
            openId = getOpenId(code);
        }
        boolean validCode = validCodeService.checkValidCode(PhoneValidMethod.LOGIN, phone, pwd);
        if (!validCode) {
            //TODO 验证码校验
            throw new AdviceMessenger(403, "验证码有误");
        }
        TbEcomMember member = new TbEcomMember();
        member.setPersonId(phone);
        member = memberDao.getOne(new QueryWrapper<>(member));
        String memberId = null;
        String name;
        if (openId != null) {
            logger.info("成功获取到了openId：[{}]", openId);
            name = getUserName(openId);
        } else {
            name = "用户";
        }
        if (member == null) {
            //TODO 获取openId
            memberId = IdUtil.getNextId();
            logger.info(String.format("用户注册开始：[%s,%s,%s]", phone, openId, memberId));
            //注册流程
            remoteRegister(phone, name, memberId);
            member = localRegister(phone, memberId, openId);
        }
        return buildMemberInfo(member);
    }

    private Token buildMemberInfo(TbEcomMember member) {

        String memberId = member.getMemberId();
        String phone = member.getPersonId();

        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, UserChnlCode.USERCHNL2001.getCode());
        if (userInf == null) {
            throw new BizException(ResultState.NOT_FOUND, "找不到用户信息,请联系客服咨询");
        }

        String token = memberId + ":" + IdUtil.getNextId();
        //TODO
        jedis2.del("TOKEN" + memberId);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(userInf.getUserId());
        memberInfo.setMemberId(memberId);
        memberInfo.setMobilePhoneNo(phone);
        memberInfo.setUserName(userInf.getUserName());
        memberInfo.setOpenId(member.getOpenId());

        TbEcomMemberAddress temp = new TbEcomMemberAddress();
        temp.setMemberId(memberId);
        //检查是否存在地址
        int count = addressDao.count(new QueryWrapper<>(temp));

        memberInfo.setHasAddress(count > 0);

        TbEcomPayCard card = new TbEcomPayCard();
        card.setMemberId(memberId);
        count = cardDao.count(new QueryWrapper<>(card));

        memberInfo.setHasCard(count > 0);
        //将获取到的token存入redis缓存;

        try {
            jedis2.hset("TOKEN" + memberId, token, ShopUtils.MAPPER.writeValueAsString(memberInfo));
            jedis2.expire("TOKEN" + memberId, 3600 * 24);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //前端测试用
        return new Token(token, memberInfo);
    }

    private String getUserName(String openId) {
        RestTemplate template1 = new RestTemplate();
        template1.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("openId", openId);
        ResponseEntity<LinkedHashMap> entity = template1.postForEntity(wxUserInfoApi, paramsMap, LinkedHashMap.class);
        if (entity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return "用户";
    }

    @Override
    public Token wxLogin(String code) {
        String openId = getOpenId(code);
        if (openId == null) {
            throw new AdviceMessenger(ResultState.UNAUTHORIZED, "您还未登录");
        }
        TbEcomMember member = new TbEcomMember();
        member.setOpenId(openId);
        member = memberDao.getOne(new QueryWrapper<>(member));
        if (member == null) {
            throw new BizException(ResultState.UNAUTHORIZED, "您还未登录");
        }
        member.setOpenId(openId);
        memberDao.updateById(member);
        return buildMemberInfo(member);
    }

    @Override
    public Integer logout() {
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new BizException(ResultState.UNAUTHORIZED, "您还未登录");
        }
        jedis2.del("TOKEN" + memberInfo.getMemberId());
        TbEcomMember query = new TbEcomMember();
        query.setMemberId(memberInfo.getMemberId());
        TbEcomMember one = memberDao.getOne(new QueryWrapper<>(query));
        if (one == null) {
            return 404;
        }
        one.setOpenId(IdUtil.getNextId());
        memberDao.updateById(one);
        return 200;
    }

    private String getOpenId(String code) {
        RestTemplate template1 = new RestTemplate();
        template1.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("code", code);
        ResponseEntity<String> stringResponseEntity = template1.postForEntity(wxCodeApi, paramsMap, String.class);
        if (stringResponseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return stringResponseEntity.getBody();
    }


    private TbEcomMember localRegister(String phone, String newMember, String openId) {
        if (openId == null) {
            openId = IdUtil.getNextId();
        }
        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, UserChnlCode.USERCHNL2001.getCode());
        TbEcomMember member = new TbEcomMember();
        //TODO 测试用
        member.setPersonId(phone);
        member.setMemberId(newMember);
        member.setOpenId(IdUtil.getNextId());
        member.setUserId(userInf.getUserId());
        member.setCreateTime(System.currentTimeMillis());
        boolean save = memberDao.save(member);
        if (!save) {
            throw new AdviceMessenger(500, "本地注册失败");
        }
        return member;
    }

    private String remoteRegister(String phone, String name, String memberId) {
        String userId = null;
        //构造VO对象
        OpenUserInfReqVo req = new OpenUserInfReqVo();
        req.setMobilePhone(phone);
        req.setTransId(TransCode.CW80.getCode());
        req.setTransChnl(TransChnl.CHANNEL6.toString());
        //TODO 获取用户ID
        req.setUserName(name);
        req.setUserType(UserType.TYPE100.getCode());
        //这里不应该是前端传递的
//        req.setCompanyId("100000000000000000000000");
        req.setUserChnl(UserChnlCode.USERCHNL1002.getCode());
        req.setUserChnlId(memberId);

        //执行注册
        BaseResult result = userInfFacade.registerUserInf(req);
        checkResult(result);
        userId = (String) result.getObject();
        if (StringUtils.isEmpty(userId)) {
            throw new AdviceMessenger(500, "注册失败，请核对您的信息");
        }
        return userId;
    }

    private void checkResult(BaseResult baseResult) {
        if (baseResult != null) {
            String code = baseResult.getCode();
            if (BaseResult.SUCCESS_CODE.equals(code)) {
                return;
            }
            throw new AdviceMessenger(500, baseResult.getMsg());
        }
        throw new AdviceMessenger(500, "通讯异常，请稍后");
    }

}
