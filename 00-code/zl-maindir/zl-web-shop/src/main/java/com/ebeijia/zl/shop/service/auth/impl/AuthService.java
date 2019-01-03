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
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
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
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
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

    @Override
    @ShopTransactional
    public Token phoneLogin(String phone, String pwd) {
        String userId = null;
        boolean validCode = validCodeService.checkValidCode(PhoneValidMethod.LOGIN, phone, pwd);
        if (!validCode) {
            //TODO 验证码校验
            throw new AdviceMessenger(403, "验证码有误");
        }
        TbEcomMember member = new TbEcomMember();
        member.setPersonId(phone);
        member = memberDao.getOne(new QueryWrapper<>(member));
        String memberId = null;

        if (member == null) {
            //TODO 获取openId
            String openId = IdUtil.getNextId();
            //注册流程
            remoteRegister(phone, openId);
            member = localRegister(phone, openId);
        }
        memberId = member.getMemberId();

        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, UserChnlCode.USERCHNL2001.getCode());
        if (userInf == null) {
            throw new BizException(ResultState.NOT_FOUND, "找不到用户信息,请联系客服咨询");
        }

        String token = memberId + ":" + IdUtil.getNextId();
        //TODO
        jedis2.del("TOKEN"+ memberId);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(userInf.getUserId());
        memberInfo.setMemberId(memberId);
        memberInfo.setMobilePhoneNo(phone);
        memberInfo.setUserName(userInf.getUserName());
        memberInfo.setOpenId(member.getOpenId());
        //将获取到的token存入redis缓存;

        try {
            jedis2.hset("TOKEN"+ memberId, token ,ShopUtils.MAPPER.writeValueAsString(memberInfo));
            jedis2.expire("TOKEN"+ memberId,3600*24);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //前端测试用
        return new Token(token);
    }

    private TbEcomMember localRegister(String phone, String openId) {
        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, UserChnlCode.USERCHNL2001.getCode());
        TbEcomMember member = new TbEcomMember();
        //TODO 测试用
        String memberId = IdUtil.getNextId();
        member.setPersonId(phone);
        member.setMemberId(memberId);
        member.setOpenId(openId);
        member.setUserId(userInf.getUserId());
        member.setCreateTime(System.currentTimeMillis());
        boolean save = memberDao.save(member);
        if (!save) {
            throw new AdviceMessenger(500, "本地注册失败");
        }
        return member;
    }

    private String remoteRegister(String phone, String openId) {
        String userId = null;

        //构造VO对象
        OpenUserInfReqVo req = new OpenUserInfReqVo();
        req.setMobilePhone(phone);
        req.setTransId(TransCode.CW80.getCode());
        req.setTransChnl(TransChnl.CHANNEL6.toString());
        //TODO 获取用户ID
        req.setUserName("用户");
        req.setUserType(UserType.TYPE100.getCode());
        //这里不应该是前端传递的
//        req.setCompanyId("100000000000000000000000");
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(openId);

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
