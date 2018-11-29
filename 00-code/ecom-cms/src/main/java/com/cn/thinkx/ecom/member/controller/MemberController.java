package com.cn.thinkx.ecom.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.member.service.MemberService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("member/memberInf")
public class MemberController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("memberService")
	private MemberService memberService;

	@RequestMapping(value = "/getMemberInfList")
	public ModelAndView getMemberInfList(HttpServletRequest req,MemberInf  memberInf) {
		ModelAndView mv = new ModelAndView("member/listMember");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<MemberInf> pageInfo = null;
		try {
			pageInfo = memberService.getMemberListPage(startNum, pageSize, memberInf);
		} catch (Exception e) {
			logger.error("## 会员信息查询出错", e);
		}
		mv.addObject("pageInfo", pageInfo);
		return mv;
	}
}
