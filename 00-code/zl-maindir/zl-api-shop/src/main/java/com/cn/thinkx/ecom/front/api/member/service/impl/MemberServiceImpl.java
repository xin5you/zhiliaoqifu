package com.cn.thinkx.ecom.front.api.member.service.impl;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.mapper.MemberInfMapper;
import com.cn.thinkx.ecom.front.api.member.service.MemberService;
import com.ebeijia.zl.common.utils.constants.ExceptionEnum;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberInfMapper memberInfMapper;

	@Override
	public BaseResult<Object> insertMember(HttpServletRequest req, HttpServletResponse resp, MemberInf entity) {
		BaseResult<Object> res = new BaseResult<Object>();
		res.setCode(ExceptionEnum.ERROR_CODE);
		res.setMsg(ExceptionEnum.ERROR_MSG);
		try {
			//从session获取
//			entity.setUserId("2018042615253900000997");
			MemberInf m = memberInfMapper.getMemberInfByUserId(entity);
			if (m == null) {
				memberInfMapper.insert(entity);
			} else {
				logger.info("该用户已成为该会员！");
				return ResultsUtil.error(ExceptionEnum.memberNews.M04.getCode(), ExceptionEnum.memberNews.M04.getMsg());
			}
			res.setCode(ExceptionEnum.SUCCESS_CODE);
			res.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.info("添加会员信息异常", e);
			return ResultsUtil.error(ExceptionEnum.memberNews.M05.getCode(), ExceptionEnum.memberNews.M05.getMsg());
		}
		return res;
	}

	@Override
	public MemberInf getMemberInfByPrimaryKey(String memberId) {
		return memberInfMapper.selectByPrimaryKey(memberId);
	}

}
