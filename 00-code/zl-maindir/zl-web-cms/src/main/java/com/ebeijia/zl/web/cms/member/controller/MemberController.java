package com.ebeijia.zl.web.cms.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.member.domain.MemberInf;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.web.cms.member.service.MemberService;
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
		try {
			PageInfo<MemberInf> pageInfo = memberService.getMemberListPage(startNum, pageSize, memberInf);
			mv.addObject("pageInfo", pageInfo);
		} catch (Exception e) {
			logger.error("## 会员信息查询出错", e);
		}
		
		return mv;
	}
}
