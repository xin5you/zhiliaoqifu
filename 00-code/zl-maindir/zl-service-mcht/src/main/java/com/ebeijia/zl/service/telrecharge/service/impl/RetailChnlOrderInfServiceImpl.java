package com.ebeijia.zl.service.telrecharge.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.service.telrecharge.enums.TelRechargeConstants;
import com.ebeijia.zl.service.telrecharge.mapper.RetailChnlOrderInfMapper;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlOrderInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlProductInfService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 分销商订单信息表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Service
public class RetailChnlOrderInfServiceImpl extends ServiceImpl<RetailChnlOrderInfMapper,RetailChnlOrderInf> implements RetailChnlOrderInfService{
	
	@Autowired
	private RetailChnlOrderInfMapper retailChnlOrderInfMapper;
	
	@Autowired
	private RetailChnlInfService retailChnlInfService;
	
	@Autowired
	private RetailChnlProductInfService retailChnlProductInfService;
	
	@Autowired
	private ProviderOrderInfService providerOrderInfService;
	
	/**
	 * 
	 * @param RetailChnlOrderInf 分销商订单
	 * @param operId 运营商
	 * @param areaName 地区名称
	 * @return
	 * @throws Exception
	 */
	public TeleRespDomain proChannelOrder(RetailChnlOrderInf retailChnlOrderInf,String operId,String areaName) throws Exception{
		
		//获取分销商信息
		RetailChnlInf retailChnlInf= retailChnlInfService.getById(retailChnlOrderInf.getChannelId());
		if(retailChnlInf ==null){
			return ResultsUtil.error("110001", "渠道号不存在");
		}
		
		//获取分销商的产品 & 折扣率
		Map maps=new HashMap<>();
		maps.put("productId", retailChnlOrderInf.getProductId());
		//maps.put("operId", operId);
		//maps.put("areaName", areaName);
		maps.put("channelId", retailChnlInf.getChannelId()); //分销商ID
		maps.put("productAmt", retailChnlOrderInf.getRechargeValue());
		maps.put("productType", retailChnlOrderInf.getRechargeType());
		
		RetailChnlProductInf telChannelProductInf =retailChnlProductInfService.getProductRateByMaps(maps);
		if(telChannelProductInf ==null){
			return ResultsUtil.error("110002", "产品不存在");
		}
		
		//产品售价 = 产品的价格 * 折扣率
		BigDecimal	payAmt= telChannelProductInf.getProductPrice().multiply(telChannelProductInf.getChannelRate()).setScale(3, BigDecimal.ROUND_DOWN);
		
//		//判断当前的备付金是否大于当前的充值的产品售价
//		if (RetailChnlInf.getChannelReserveAmt().compareTo(payAmt)== -1){
//			return ResultsUtil.error("110003", "备付金不足");
//		}
		
		retailChnlOrderInf.setPayAmt(payAmt);
		
		//扣减备付金 
	   //	int resOper=RetailChnlInfFacade.subChannelReserveAmt(RetailChnlInf.getChannelId(),payAmt);
		//保存订单o.toString()o.toString()o.toString()
		
		retailChnlOrderInf.setDataStat("0"); //
		retailChnlOrderInf.setOrderStat(TeleConstants.ChannelOrderPayStat.ORDER_PAY_0.getCode()); //待扣款
		retailChnlOrderInf.setItemNum(1);//产品数量
		retailChnlOrderInf.setChannelRate(telChannelProductInf.getChannelRate()); //折扣率
		retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_1.getCode());  //处理中
			if(telChannelProductInf !=null){
				retailChnlOrderInf.setProductId(telChannelProductInf.getProductId());
			}
			if(StringUtil.isNotEmpty(retailChnlOrderInf.getNotifyUrl())){
				retailChnlOrderInf.setNotifyFlag("0");
			}else{
				retailChnlOrderInf.setNotifyFlag("1");
			}
			boolean resOper=this.save(retailChnlOrderInf); //保存分销商订单
			
			ProviderOrderInf providerOrderInf=null;
			if(resOper){
				providerOrderInf=new ProviderOrderInf();
				providerOrderInf.setRegOrderAmt(retailChnlOrderInf.getRechargeValue()); //充值面额
				providerOrderInf.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
				providerOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_8.getCode()); //待充值
				providerOrderInf.setDataStat("0");
				resOper=providerOrderInfService.save(providerOrderInf); //保存供应商订单
		}
		if(!resOper){
			//操作不同步，则回退事物
			throw new RuntimeException();
		}
		
		//返回成功的封装数据
		TeleRespVO respVo=new TeleRespVO();
		respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
		respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
		respVo.setPayState(retailChnlOrderInf.getOrderStat());
		respVo.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
		respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date()));
		respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
		respVo.setItemNum(retailChnlOrderInf.getItemNum());
		respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
		return ResultsUtil.success(respVo);
	}
	
	public List<RetailChnlOrderInf> getRetailChnlOrderInfList(RetailChnlOrderInf retailChnlOrderInf) throws Exception {
		return retailChnlOrderInfMapper.getRetailChnlOrderInfList(retailChnlOrderInf);
	}
	
	public void doRechargeMobileMsg(String channelOrderId){
//		rechargeMobileProducerService.sendRechargeMobileMsg(channelOrderId);
	}
	/**
	 *  分销商 根据外部订单查询
	 * @param outerId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public RetailChnlOrderInf getRetailChnlOrderInfByOuterId(String outerId,String channelId) throws Exception{
		return retailChnlOrderInfMapper.getRetailChnlOrderInfByOuterId(outerId,channelId);
	}
	
	/**
	 * 分销商订单分页列表
	 * @param startNum
	 * @param pageSize
	 * @param RetailChnlOrderInf
	 * @return
	 * @throws Exception 
	 */
     public PageInfo<RetailChnlOrderInf> getRetailChnlOrderInfPage(int startNum, int pageSize, RetailChnlOrderInf RetailChnlOrderInf) throws Exception{
    		PageHelper.startPage(startNum, pageSize);
    		List<RetailChnlOrderInf> RetailChnlOrderInfList = getRetailChnlOrderInfList(RetailChnlOrderInf);
    		for (RetailChnlOrderInf RetailChnlOrderInf2 : RetailChnlOrderInfList) {
				if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getRechargeType()))
					RetailChnlOrderInf2.setRechargeType(TelRechargeConstants.ShopType.findByCode(RetailChnlOrderInf2.getRechargeType()));
				if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getOrderStat()))
					RetailChnlOrderInf2.setOrderStat(TelRechargeConstants.ChannelOrderStat.findByCode(RetailChnlOrderInf2.getOrderStat()));
				if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getNotifyStat()))
					RetailChnlOrderInf2.setNotifyStat(TelRechargeConstants.ChannelOrderNotifyStat.findByCode(RetailChnlOrderInf2.getNotifyStat()));
			}
    		PageInfo<RetailChnlOrderInf> RetailChnlOrderInfPage = new PageInfo<RetailChnlOrderInf>(RetailChnlOrderInfList);
    		return RetailChnlOrderInfPage;
     }

	@Override
	public PageInfo<RetailChnlOrderInf> getRetailChnlOrderInf(int startNum, int pageSize, RetailChnlOrderInf retailChnlOrderInf) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<RetailChnlOrderInf> retailChnlOrderInfList = retailChnlOrderInfMapper.getRetailChnlOrderInfList(retailChnlOrderInf);
		for (RetailChnlOrderInf RetailChnlOrderInf2 : retailChnlOrderInfList) {
			if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getRechargeState()))
				RetailChnlOrderInf2.setRechargeState(TelRechargeConstants.providerOrderRechargeState.findByCode(RetailChnlOrderInf2.getRechargeState()));
		}
		PageInfo<RetailChnlOrderInf> RetailChnlOrderInfPage = new PageInfo<RetailChnlOrderInf>(retailChnlOrderInfList);
		return RetailChnlOrderInfPage;
	}

//	@Override
//	public List<RetailChnlOrderInfUpload> getRetailChnlOrderInfListToUpload(RetailChnlOrderInf order) {
//		List<RetailChnlOrderInfUpload> list = retailChnlOrderInfMapper.getRetailChnlOrderInfListToUpload(order);
//		for (RetailChnlOrderInfUpload RetailChnlOrderInf2 : list) {
//			if(!StringUtil.isNullOrEmpty(RetailChnlOrderInf2.getRechargeState()))
//				RetailChnlOrderInf2.setRechargeState(TelRechargeConstants.providerOrderRechargeState.findByCode(RetailChnlOrderInf2.getRechargeState()));
//		}
//		return list;
//	}

	@Override
	public RetailChnlOrderInf getRetailChnlOrderInfCount(RetailChnlOrderInf order) {
		return retailChnlOrderInfMapper.getRetailChnlOrderInfCount(order);
	}

}
