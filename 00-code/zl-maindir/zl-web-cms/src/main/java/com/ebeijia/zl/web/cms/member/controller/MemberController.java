package com.ebeijia.zl.web.cms.member.controller;

import javax.servlet.http.HttpServletRequest;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.web.cms.member.service.MemberService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("member")
public class MemberController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberService memberService;

	/**
	 * 查询会员信息（含分页）
	 * @param req
	 * @param memberInf
	 * @return
	 */
	@RequestMapping(value = "/getMemberInfList")
	public ModelAndView getMemberInfList(HttpServletRequest req, TbEcomMember memberInf) {
		ModelAndView mv = new ModelAndView("member/listMember");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<TbEcomMember> pageInfo = memberService.getMemberListPage(startNum, pageSize, memberInf);
			mv.addObject("pageInfo", pageInfo);
		} catch (Exception e) {
			logger.error("## 会员信息查询出错", e);
		}
		mv.addObject("memberInf", memberInf);
		return mv;
	}
}
