package com.ebeijia.zl.shop.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
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
import com.ebeijia.zl.shop.vo.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
    public Token phoneLogin(String phone, String pwd) {
        String userId = null;
        //TODO 验证码校验
        boolean validCode = validCodeService.checkValidCode(PhoneValidMethod.LOGIN, phone, pwd);
        if (!validCode) {
            throw new AdviceMessenger(403, "验证码有误");
        }
        TbEcomMember member = new TbEcomMember();
        member.setPersonId(phone);
        member = memberDao.getOne(new QueryWrapper<>(member));
        if (member == null) {
            //注册流程
            remoteRegister(phone);
            localRegister(phone);
        }
        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, TransChnl.CHANNEL6.toString());
        if (userInf == null) {
            throw new AdviceMessenger(ResultState.NOT_FOUND, "找不到用户信息,请联系客服咨询");
        }
        //测试用
        HashMap<String, String> token = new HashMap<>();
        token.put("userid", "TT233");
        token.put("token", "testToken");


        //将获取到的token存入redis缓存;
        jedis2.set(token.get("token"), token.get("userid"), 3600 * 24);
        //前端测试用
        return new Token(token.get("token"));
    }

    private void localRegister(String phone) {
        UserInf userInf = userInfFacade.getUserInfByPhoneNo(phone, TransChnl.CHANNEL6.toString());
        TbEcomMember member = new TbEcomMember();
        //TODO 测试用
        member.setPersonId(phone);
        member.setMemberId(IdUtil.getNextId());
        member.setUserId(userInf.getUserId());
        member.setCreateTime(System.currentTimeMillis());
        boolean save = memberDao.save(member);
        if (!save){
            throw new AdviceMessenger(500,"本地注册失败");
        }
    }

    private String remoteRegister(String phone) {
        String userId = null;
        OpenUserInfReqVo req = new OpenUserInfReqVo();
        req.setMobilePhone(phone);

        req.setTransId(TransCode.CW80.getCode());
        req.setTransChnl("40001001");
        //TODO 获取用户ID
        req.setUserName("用户");
        req.setUserType(UserType.TYPE100.getCode());
        req.setCompanyId("100000000000000000000000");
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(IdUtil.getNextId());

        BaseResult result = userInfFacade.registerUserInf(req);
        checkResult(result);
        userId = (String) result.getObject();
        if (StringUtils.isEmpty(userId)){
            throw new AdviceMessenger(500,"注册失败，请核对您的信息");
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
