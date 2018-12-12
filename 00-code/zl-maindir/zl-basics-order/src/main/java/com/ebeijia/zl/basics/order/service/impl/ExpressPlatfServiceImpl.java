package com.ebeijia.zl.basics.order.service.impl;

import com.ebeijia.zl.basics.order.domain.ExpressPlatf;
import com.ebeijia.zl.basics.order.mapper.ExpressPlatfMapper;
import com.ebeijia.zl.basics.order.service.ExpressPlatfService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("expressPlatfService")
public class ExpressPlatfServiceImpl extends BaseServiceImpl<ExpressPlatf> implements ExpressPlatfService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExpressPlatfMapper expressPlatfMapper;

	@Override
	public List<ExpressPlatf> getExpressPlatfList(ExpressPlatf ep) {
		return expressPlatfMapper.getExpressPlatfList(ep);
	}

	/**
	 * 根据渠道号和包裹Id查询订单包裹信息
	 * 
	 * @param ecomCode
	 *            商户标识
	 * @param packageNo
	 *            包裹号
	 * @return
	 */
	public ExpressPlatf selectByEcomAndPackageNo(String ecomCode, String packageNo) {
		return expressPlatfMapper.selectByEcomAndPackageNo(ecomCode, packageNo);
	}

	/**
	 * 保存订单包裹信息
	 * 
	 * @param ep
	 * @return
	 */
	public ExpressPlatf saveExpressPlatfForPackageNo(ExpressPlatf ep) {
		ExpressPlatf expressPlatf = null;

		try {
			// 查询是否存在
			if (StringUtil.isNotEmpty(ep.getPackId())) {
				expressPlatf = expressPlatfMapper.selectByPrimaryKey(ep.getPackId());

			} else if (StringUtil.isNotEmpty(ep.getEcomCode()) && StringUtil.isNotEmpty(ep.getPackageNo())) {
				expressPlatf = expressPlatfMapper.selectByEcomAndPackageNo(ep.getEcomCode(), ep.getPackageNo());
			}
			// save or update
			if (expressPlatf == null) {
				expressPlatfMapper.insert(ep);
				expressPlatf = ep;
			} else {
				expressPlatf.setDeliveryTime(ep.getDeliveryTime());
				expressPlatf.setPackageStat(ep.getPackageStat());
				expressPlatf.setPackageStatDesc(ep.getPackageStatDesc());
				expressPlatf.setExpressCompanyId(ep.getExpressCompanyId());
				expressPlatf.setExpressCompanyName(ep.getExpressCompanyName());
				expressPlatf.setExpressNo(ep.getExpressNo());
				expressPlatf.setIsSign(ep.getIsSign());
				expressPlatf.setSignTime(ep.getSignTime());
				expressPlatfMapper.updateByPrimaryKey(expressPlatf);
			}
		} catch (Exception e) {
			logger.error("## 保存订单包裹信息异常", e);
		}
		return expressPlatf;
	}

	@Override
	public List<ExpressPlatf> getOrderExpressPlatfBySOrderId(String sOrderId) {
		return expressPlatfMapper.getOrderExpressPlatfBySOrderId(sOrderId);
	}

	@Override
	public List<ExpressPlatf> getExpressPlatfProductByPackId(String packId) {
		return expressPlatfMapper.getExpressPlatfProductByPackId(packId);
	}

	@Override
	public List<ExpressPlatf> getExpressPlatfBySignTimeJob() {
		return expressPlatfMapper.getExpressPlatfBySignTimeJob();
	}

	@Override
	public List<ExpressPlatf> getDeliveryTimeByItemIdAndSorderId(ExpressPlatf ep) {
		return expressPlatfMapper.getDeliveryTimeByItemIdAndSorderId(ep);
	}
}