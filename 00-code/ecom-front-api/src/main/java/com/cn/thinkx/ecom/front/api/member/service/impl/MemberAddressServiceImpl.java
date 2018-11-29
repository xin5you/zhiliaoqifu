package com.cn.thinkx.ecom.front.api.member.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.basics.member.service.MemberAddressService;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.member.service.MemberAddressInfService;

@Service
public class MemberAddressServiceImpl implements MemberAddressInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberAddressService memberAddressService;

	@Override
	public BaseResult<Object> addMemberAddress(String address, String addressId, MemberAddress memberAddre) {
		BaseResult<Object> res = new BaseResult<Object>();
		res.setCode(ExceptionEnum.ERROR_CODE);
		res.setMsg(ExceptionEnum.ERROR_MSG);
		memberAddre.setDataStat("0");
		try {
			MemberAddress memberAddress = memberAddressService.getMemberDefAddr(memberAddre.getMemberId());
			if (StringUtil.isNullOrEmpty(memberAddress)) {
				memberAddre.setDefAddr("0");
			} else {
				memberAddre.setDefAddr("1");
			}
			String[] addressIdArray = addressId.split(","); // 拆分区域Id
			String[] addressArray = address.split("，"); // 拆分区域
			memberAddre.setProvinceId(StringUtil.nullToString(addressIdArray[0]));
			memberAddre.setCityId(StringUtil.nullToString(addressIdArray[1]));
			memberAddre.setRegionId(StringUtil.nullToString(addressIdArray[2]));
			memberAddre.setProvince(StringUtil.nullToString(addressArray[0])); // 省
			memberAddre.setCity(StringUtil.nullToString(addressArray[1])); // 市
			memberAddre.setRegion(StringUtil.nullToString(addressArray[2]));// 县
			if (addressIdArray.length == 4) {
				memberAddre.setTown(StringUtil.nullToString(addressArray[3])); // 镇
				memberAddre.setTownId(StringUtil.nullToString(addressIdArray[3]));
			}
			int count = memberAddressService.getMemberAddressByMemberIdCount(memberAddre.getMemberId());
			if (count < 5) {
				memberAddressService.insert(memberAddre);
			} else {
				logger.error("## 收货地址最多只能添加五个");
				return ResultsUtil.error(ExceptionEnum.memberNews.M02.getCode(), ExceptionEnum.memberNews.M02.getMsg());
			}
			res.setCode(ExceptionEnum.SUCCESS_CODE);
			res.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.error("## 添加收货地址出错", e);
			return ResultsUtil.error(ExceptionEnum.memberNews.M01.getCode(), ExceptionEnum.memberNews.M01.getMsg());
		}
		return res;
	}

	@Override
	public BaseResult<Object> updateMemberAddress(String address, String addressId, MemberAddress memberAddre) {
		BaseResult<Object> res = new BaseResult<Object>();
		res.setCode(ExceptionEnum.ERROR_CODE);
		res.setMsg(ExceptionEnum.ERROR_MSG);
		try {
			MemberAddress memberAdd = getMemberAddressByAddrId(memberAddre.getAddrId());
			String[] addressArray = address.split("，"); // 拆分区域
			if (!StringUtil.isNullOrEmpty(addressId)) {
				String[] addressIdArray = addressId.split(","); // 拆分区域Id
				memberAdd.setProvinceId(StringUtil.nullToString(addressIdArray[0]));
				memberAdd.setCityId(StringUtil.nullToString(addressIdArray[1]));
				memberAdd.setRegionId(StringUtil.nullToString(addressIdArray[2]));
				if (addressIdArray.length == 4) {
					memberAdd.setTownId(StringUtil.nullToString(addressIdArray[3]));
				}
			}
			memberAdd.setProvince(StringUtil.nullToString(addressArray[0])); // 省
			memberAdd.setCity(StringUtil.nullToString(addressArray[1])); // 市
			memberAdd.setRegion(StringUtil.nullToString(addressArray[2]));// 县
			memberAdd.setUserName(memberAddre.getUserName());
			memberAdd.setMobile(memberAddre.getMobile());
			memberAdd.setAddrDetail(memberAddre.getAddrDetail());
			if (addressArray.length == 4) {
				memberAdd.setTown(StringUtil.nullToString(addressArray[3])); // 镇
			}
			//如果修改的是默认地址，设置标识为"0"
			if("0".equals(memberAdd.getDefAddr())){
				memberAdd.setDefAddr("0");
			}else{
				memberAdd.setDefAddr(memberAddre.getDefAddr());
			}
			//如果修改地址为默认地址，先修改原来的默认地址
			if("0".equals(memberAddre.getDefAddr())){
				MemberAddress memberDefAddress = memberAddressService.getMemberDefAddr(memberAdd.getMemberId()); // 获取默认的收货信息
				if (memberDefAddress != null) {
					memberDefAddress.setDefAddr("1");
					memberAddressService.updateByPrimaryKey(memberDefAddress); // 把原来的默认地址修改不默认
				}
			}
			memberAddressService.updateByPrimaryKey(memberAdd);
			res.setCode(ExceptionEnum.SUCCESS_CODE);
			res.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.error("## 修改收货地址出错", e);
			return ResultsUtil.error(ExceptionEnum.memberNews.M03.getCode(), ExceptionEnum.memberNews.M03.getMsg());
		}
		return res;
	}

	@Override
	public List<MemberAddress> getMemberAddressList(MemberAddress memberAddre) {
		List<MemberAddress> memberAddressList = null;
		try {
			memberAddressList = memberAddressService.getList(memberAddre);
		} catch (Exception e) {
			logger.error("## 查詢收货地址出错", e);
		}
		return memberAddressList;
	}

	@Override
	public MemberAddress getMemberDefAddr(String memberId) {
		MemberAddress memberAddress = null;
		try {
			memberAddress = memberAddressService.getMemberDefAddr(memberId);
		} catch (Exception e) {
			logger.error("## 查询会员收货的默认地址异常", e);
		}
		return memberAddress;

	}

	@Override
	public BaseResult<Object> deleteMemberAddress(String addrId) {
		BaseResult<Object> res = new BaseResult<Object>();
		res.setCode(ExceptionEnum.ERROR_CODE);
		res.setMsg(ExceptionEnum.ERROR_MSG);
		try {
			if (StringUtil.isNullOrEmpty(addrId)) {
				logger.error("## 删除会员收货地址出错,会员地址addrId为空：[{}]", addrId);
				return ResultsUtil.error(ExceptionEnum.memberNews.M03.getCode(), ExceptionEnum.memberNews.M03.getMsg());
			}
			int i = memberAddressService.deleteByPrimaryKey(addrId);
			if (i < 0) {
				logger.error("## 删除会员收货地址出错,会员地址addrId：[{}]", addrId);
				return ResultsUtil.error(ExceptionEnum.memberNews.M03.getCode(), ExceptionEnum.memberNews.M03.getMsg());
			}
			res.setCode(ExceptionEnum.SUCCESS_CODE);
			res.setMsg(ExceptionEnum.SUCCESS_MSG);
		} catch (Exception e) {
			logger.error("## 删除会员收货地址出错,会员地址addrId：[{}]", addrId);
			return ResultsUtil.error(ExceptionEnum.memberNews.M03.getCode(), ExceptionEnum.memberNews.M03.getMsg());
		}
		return res;
	}

//	@Override
//	public BaseResult<Object> updateMemberDefAddr(String addrId) {
//		try {
//			if (StringUtil.isNullOrEmpty(addrId)) {
//				ResultsUtil.error(ExceptionEnum.memberNews.M07.getCode(), ExceptionEnum.memberNews.M07.getMsg());
//			}
//			MemberAddress memberaddre = memberAddressService.selectByPrimaryKey(addrId); // 获取设置为默认的收货地址信息
//			MemberAddress memberAddress = memberAddressService.getMemberDefAddr(memberaddre.getMemberId()); // 获取默认的收货信息
//			if (memberAddress != null) {
//				memberAddress.setDefAddr("1");
//				memberAddressService.updateByPrimaryKey(memberAddress); // 把原来的默认地址修改不默认
//			}
//			memberaddre.setDefAddr("0");
//			memberAddressService.updateByPrimaryKey(memberaddre); // 修改默认地址
//		} catch (Exception e) {
//			logger.error("## 修改会员默认收货地址出错，地址addrId：[{}]", addrId, e);
//			return ResultsUtil.error(ExceptionEnum.memberNews.M07.getCode(), ExceptionEnum.memberNews.M07.getMsg());
//		}
//		return ResultsUtil.success();
//	}

	@Override
	public MemberAddress getMemberAddressByAddrId(String addrId) {
		MemberAddress m = new MemberAddress();
		try {
			m = memberAddressService.selectByPrimaryKey(addrId);
		} catch (Exception e) {
			logger.error("## 获取默认地址失败", e);
		}
		return m;
	}

	@Override
	public int getMemberAddressByMemberIdCount(String memberId) {
		return memberAddressService.getMemberAddressByMemberIdCount(memberId);
	}

}
