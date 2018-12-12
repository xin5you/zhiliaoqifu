package com.ebeijia.zl.service.telrecharge.facadeimpl;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlOrderInfService;
import com.github.pagehelper.PageInfo;

@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
@Service
public class RetailChnlOrderInfFacadeImpl  implements RetailChnlOrderInfFacade {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RetailChnlOrderInfService retailChnlOrderInfService;
	
//	@Autowired
//	private RetailChnlInfService retailChnlInfService;
//	
//	@Autowired
//	private RetailChnlProductInfService retailChnlProductInfService;
//
//	@Autowired
//	private WechatMQProducerService rechargeMobileProducerService;
	
	@Override
	public RetailChnlOrderInf getRetailChnlOrderInfById(String channelOrderId) throws Exception {
		return retailChnlOrderInfService.getById(channelOrderId);
	}

	@Override
	public boolean saveRetailChnlOrderInf(RetailChnlOrderInf retailChnlOrderInf) throws Exception {
		 return retailChnlOrderInfService.save(retailChnlOrderInf);
	}

	@Override
	public boolean updateRetailChnlOrderInf(RetailChnlOrderInf RetailChnlOrderInf) throws Exception {
		return retailChnlOrderInfService.updateById(RetailChnlOrderInf);
	}

	@Override
	public boolean deleteRetailChnlOrderInfById(String channelOrderId) throws Exception {
		return retailChnlOrderInfService.removeById(channelOrderId);
	}
	
	/**
	 * 
	 * @param RetailChnlOrderInf 分销商订单
	 * @param operId 运营商
	 * @param areaName 地区名称
	 * @return
	 * @throws Exception
	 */
	public TeleRespDomain proChannelOrder(RetailChnlOrderInf retailChnlOrderInf,String operId,String areaName) throws Exception{
		return retailChnlOrderInfService.proChannelOrder(retailChnlOrderInf, operId, areaName);
	}
	
	public List<RetailChnlOrderInf> getRetailChnlOrderInfList(RetailChnlOrderInf RetailChnlOrderInf) throws Exception {
		return retailChnlOrderInfService.getRetailChnlOrderInfList(RetailChnlOrderInf);
	}
	
	public void doRechargeMobileMsg(String channelOrderId){
//		retailChnlOrderInfService.sendRechargeMobileMsg(channelOrderId);
	}
	/**
	 *  分销商 根据外部订单查询
	 * @param outerId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public RetailChnlOrderInf getRetailChnlOrderInfByOuterId(String outerId,String channelId) throws Exception{
		return retailChnlOrderInfService.getRetailChnlOrderInfByOuterId(outerId,channelId);
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
    	 return retailChnlOrderInfService.getRetailChnlOrderInfPage(startNum, pageSize, RetailChnlOrderInf);
     }

	@Override
	public PageInfo<RetailChnlOrderInf> getRetailChnlOrderInf(int startNum, int pageSize, RetailChnlOrderInf RetailChnlOrderInf) throws Exception {
		return retailChnlOrderInfService.getRetailChnlOrderInf(startNum, pageSize, RetailChnlOrderInf);
	}

//	@Override
//	public List<RetailChnlOrderInfUpload> getRetailChnlOrderInfListToUpload(RetailChnlOrderInf order) {
//		return retailChnlOrderInfService.getRetailChnlOrderInfListToUpload(order);
//	}

	@Override
	public RetailChnlOrderInf getRetailChnlOrderInfCount(RetailChnlOrderInf order) {
		return retailChnlOrderInfService.getRetailChnlOrderInfCount(order);
	}
}
