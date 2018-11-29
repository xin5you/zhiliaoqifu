package com.cn.thinkx.ecom.front.api.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.member.service.MemberAddressInfService;

@RestController
@RequestMapping(value = "member/memberAddress")
public class MemberAddressController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberAddressInfService memberAddressService;

	/**
	 * 查询该会员下的所有收货地址（参数：会员id）
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getMemberAddressListByMemberId")
	public ModelAndView getMemberAddressListByMemberId(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("ecom/member/memberAddress");
		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		MemberAddress memberAddre = new MemberAddress();
		memberAddre.setMemberId(memberId);
		List<MemberAddress> memberAddreList = memberAddressService.getMemberAddressList(memberAddre);
		mv.addObject("memberAddreList", memberAddreList);
		return mv;
	}
	


	@RequestMapping(value = "/intoAddMemberAddress")
	public ModelAndView intoAddMemberAddress(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("ecom/member/addMemberAddress");
		return mv;
	}

	/**
	 * 添加会员收货地址
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addMemberAddress")
	public BaseResult<Object> addMemberAddress(HttpServletRequest req, HttpServletResponse resp,
			MemberAddress memberAddre) {
		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		memberAddre.setMemberId(memberId);
		String address = req.getParameter("address"); // 获取所在区域
		String addressId = req.getParameter("addressId"); // 获取所在区域id
		return memberAddressService.addMemberAddress(address, addressId, memberAddre);
	}

	/**
	 * 查询默认收货地址
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/getMemberAddressdef")
	public ModelAndView getMemberAddressdef(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView();
		String memberId = req.getParameter("memberId");
		MemberAddress memberAddress = memberAddressService.getMemberDefAddr(memberId);
		mv.addObject("memberAddress", memberAddress);
		return mv;
	}

	/**
	 * 进入收货地址编辑页面
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/intoEditMemberAddress")
	public ModelAndView intoEditMemberAddress(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("ecom/member/editMemberAddress");
		String addrId = req.getParameter("addrId");
		if (StringUtil.isNullOrEmpty(addrId)) {
			return null;
		}
		MemberAddress memberAddress = memberAddressService.getMemberAddressByAddrId(addrId);
		mv.addObject("memberAddress", memberAddress);
		return mv;
	}

	/**
	 * 修改收货地址
	 * 
	 * @param req
	 * @param resp
	 * @param memberAddre
	 */
	@RequestMapping(value = "/updateMemberAddress")
	public BaseResult<Object> updateMemberAddress(HttpServletRequest req, HttpServletResponse resp,
			MemberAddress memberAddre) {
		String address = req.getParameter("address"); // 获取所在区域
		String addressId = req.getParameter("addressId"); // 获取所在区域id
		return memberAddressService.updateMemberAddress(address, addressId, memberAddre);
	}

	/**
	 * 删除收货地址
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/deleteMemberAddress")
	public BaseResult<Object> deleteMemberAddress(HttpServletRequest req, HttpServletResponse resp) {
		String addrId = req.getParameter("addrId");
		return memberAddressService.deleteMemberAddress(addrId);
	}

//	/**
//	 * 修改默认的收货地址
//	 * 
//	 * @param req
//	 * @param resp
//	 * @param addrId
//	 * @return
//	 */
//	@RequestMapping(value = "/updateMemberDefAddr")
//	public BaseResult<Object> updateMemberDefAddr(HttpServletRequest req, HttpServletResponse resp, String addrId) {
//		return memberAddressService.updateMemberDefAddr(addrId);
//	}
	
	/**
	 * 查询该会员下的所有收货地址（参数：会员id）json格式
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getMemberAddressListJson")
	public BaseResult<Object> getMemberAddressListJson(HttpServletRequest req) {
		BaseResult<Object> result=new BaseResult<Object>();
		String memberId=StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		MemberAddress memberAddre = new MemberAddress();
		memberAddre.setMemberId(memberId);
		List<MemberAddress> memberAddreList = memberAddressService.getMemberAddressList(memberAddre);
		
		result.setCode(ExceptionEnum.SUCCESS_CODE);
		result.setMsg(ExceptionEnum.SUCCESS_MSG);
		result.setObject(memberAddreList);
		return result;
	}
	
	/**
	 * 查询该会员收货地址（参数：addrId）json格式
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getMemberAddressByIdJson")
	public BaseResult<Object> getMemberAddressByIdJson(HttpServletRequest req) {
		BaseResult<Object> result=new BaseResult<Object>();

		String addrId=req.getParameter("addrId"); //
		
		MemberAddress memberAddre = memberAddressService.getMemberAddressByAddrId(addrId);
		result.setCode(ExceptionEnum.SUCCESS_CODE);
		result.setMsg(ExceptionEnum.SUCCESS_MSG);
		result.setObject(memberAddre);
		return result;
	}
	
	/**
	 * 查询该会员收货地址（参数：addrId）json格式
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/editMemberAddressAjax")
	public BaseResult<Object> editMemberAddressAjax(HttpServletRequest req,MemberAddress memberAddre) {
		BaseResult<Object> result=new BaseResult<Object>();

		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
	
		String address = req.getParameter("address"); // 获取所在区域
		String addressId = req.getParameter("addressId"); // 获取所在区域id
		
		String flag=req.getParameter("flag"); //
		if("0".equals(flag)){
			memberAddre.setMemberId(memberId);
			return memberAddressService.addMemberAddress(address, addressId, memberAddre);
		}else{

			return memberAddressService.updateMemberAddress(address, addressId, memberAddre);
		}
	}
	
	/**
	 * 校验收货地址不能超过五个
	 * 
	 * @return
	 */
	@PostMapping(value="/getCheckMemberAddressCount")
	public BaseResult<Object> getCheckMemberAddressCount(HttpServletRequest req){
		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		try {
			int count = memberAddressService.getMemberAddressByMemberIdCount(memberId);
			if (count > 4) {
				logger.error("## 收货地址最多只能添加五个");
				return ResultsUtil.error(ExceptionEnum.memberNews.M02.getCode(), ExceptionEnum.memberNews.M02.getMsg());
			} 
		} catch (Exception e) {
			logger.error("## 添加收货地址出错", e);
			return ResultsUtil.error(ExceptionEnum.memberNews.M01.getCode(), ExceptionEnum.memberNews.M01.getMsg());
		}
		return ResultsUtil.success();
	}
}
