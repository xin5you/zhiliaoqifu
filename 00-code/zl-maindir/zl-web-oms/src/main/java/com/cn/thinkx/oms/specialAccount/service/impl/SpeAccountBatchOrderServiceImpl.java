package com.cn.thinkx.oms.specialAccount.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderListMapper;
import com.cn.thinkx.oms.specialAccount.mapper.SpeAccountBatchOrderMapper;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransChnl;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("speAccountBatchOrderService")
public class SpeAccountBatchOrderServiceImpl implements SpeAccountBatchOrderService {

	Logger logger = LoggerFactory.getLogger(SpeAccountBatchOrderServiceImpl.class);
	
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	@Autowired
	private SpeAccountBatchOrderListMapper speAccountBatchOrderListMapper;
	
	@Autowired
	private SpeAccountBatchOrderMapper speAccountBatchOrderMapper;
	
	@Autowired
	@Qualifier("companyInfService")
	private CompanyInfService companyInfService;
	
	@Autowired
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;
	
	@Reference(check=false)
	private AccountManageFacade accountManageFacade;
	
	@Override
	public List<SpeAccountBatchOrder> getSpeAccountBatchOrderList(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.getSpeAccountBatchOrderList(order);
	}

	@Override
	public int addSpeAccountBatchOrder(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);
	}

	@Override
	public int updateSpeAccountBatchOrder(SpeAccountBatchOrder order) {
		return speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
	}

	@Override
	public int deleteSpeAccountBatchOrder(String orderId) {
		return speAccountBatchOrderMapper.deleteSpeAccountBatchOrder(orderId);
	}

	@Override
	public PageInfo<SpeAccountBatchOrder> getSpeAccountBatchOrderPage(int startNum, int pageSize, SpeAccountBatchOrder order, HttpServletRequest req) {
		PageHelper.startPage(startNum, pageSize);
		order.setOrderId(StringUtil.nullToString(req.getParameter("orderId")));//订单号
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));//订单名称
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		order.setAccountType(StringUtil.nullToString(req.getParameter("accountType")));
		order.setOrderStat(StringUtil.nullToString(req.getParameter("orderStat")));//订单状态
		order.setStartTime(StringUtil.nullToString(req.getParameter("startTime")));//开始时间
		order.setEndTime(StringUtil.nullToString(req.getParameter("endTime")));//结束时间
		List<SpeAccountBatchOrder> list = getSpeAccountBatchOrderList(order);
		PageInfo<SpeAccountBatchOrder> page = null;
		if(list != null){
			list.forEach(batchOrder ->{
				HttpSession session = req.getSession();
				User user = (User)session.getAttribute(Constants.SESSION_USER);
				batchOrder.setCreateUser(user.getId());
				batchOrder.setOrderStat(BatchOrderStat.findStat(batchOrder.getOrderStat()));
				batchOrder.setAccountType(UserType.findByCode(batchOrder.getAccountType()).getValue());
				if (!StringUtil.isNullOrEmpty(batchOrder.getSumAmount())) {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(batchOrder.getSumAmount()));
				} else {
					batchOrder.setSumAmount(NumberUtils.RMBCentToYuan(0));
				}
			});
			page = new PageInfo<SpeAccountBatchOrder>(list);
		}
		return page;
	}

	@Override
	public int addSpeAccountBatchOrder(SpeAccountBatchOrder order, LinkedList<SpeAccountBatchOrderList> personInfList) {
		int orderResult = 0;
		int orderListResult = 0;
		try {
			orderResult = speAccountBatchOrderMapper.addSpeAccountBatchOrder(order);//添加订单数据
			for (SpeAccountBatchOrderList orderList : personInfList) {
				orderList.setOrderListId(UUID.randomUUID().toString());
				orderList.setOrderId(order.getOrderId());
				orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
				orderList.setCreateUser(order.getCreateUser());
				orderList.setUpdateUser(order.getUpdateUser());
				orderList.setCreateTime(System.currentTimeMillis());
				orderList.setUpdateTime(System.currentTimeMillis());
			}
			orderListResult = speAccountBatchOrderListMapper.addSpeAccountBatchOrderList(personInfList);//添加订单明细
			//添加订单明细
			if (orderResult > 0 && orderListResult > 0) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("批量开户数据提交出错a---->>{},b----->>{}", orderResult, orderListResult, e);
		}
		return 0;
	}

	@Override
	public SpeAccountBatchOrder getSpeAccountBatchOrderByOrderId(String orderId) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderByOrderId(orderId);
		if (!StringUtil.isNullOrEmpty(order.getCompanyId())) {
			CompanyInf companyInf = companyInfService.getCompanyInfById(order.getCompanyId());
			order.setCompanyName(companyInf.getName());
		}
		order.setAccountTypeName(UserType.findByCode(order.getAccountType()).getValue());
		if (order.getSumAmount() == null || "".equals(order.getSumAmount())) {
			order.setSumAmount(NumberUtils.RMBCentToYuan("0"));
		} else {
			order.setSumAmount(NumberUtils.RMBCentToYuan(order.getSumAmount()));
		}
		return order;
	}

	@Override
	public SpeAccountBatchOrder getSpeAccountBatchOrderById(String orderId) {
		return speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
	}
	
	public List<SpeAccountBatchOrderList> getBatchOrderList(String orderId, String commitStat) {
		List<SpeAccountBatchOrderList> list = null;
		if (BatchOrderStat.BatchOrderStat_10.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(orderId);
		}
		if (BatchOrderStat.BatchOrderStat_99.getCode().equals(commitStat)) {
			list = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListFailListByOrderId(orderId);
		}
		return list;
	}

	@Override
	public int batchSpeAccountBatchOpenAccountITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if (order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat())) {
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order); // 修改订单状态
		}
		List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
		int i = 0 ;
		if (speList != null && speList.size() > 0) {
			for (SpeAccountBatchOrderList batchOrder : speList) {
				AccountOpenReqVo reqVo = new AccountOpenReqVo();
				reqVo.setUserName(batchOrder.getUserName());
				reqVo.setMobilePhone(batchOrder.getPhoneNo());
				reqVo.setIcardNo(batchOrder.getUserCardNo());
				reqVo.setCompanyId(batchOrder.getCompanyId());
				reqVo.setTransId(TransCode.CW80.getCode());
				reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
				reqVo.setUserId(batchOrder.getCompanyId());
				Set<String> bIds = new TreeSet<>();
				String[] str = order.getBizType().split(",");
				for (String s : str) {
					bIds.add(s);
				}
				reqVo.setbIds(bIds);
				reqVo.setUserType(order.getAccountType());
				reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
				reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
				try {
					BaseResult result = accountManageFacade.createAccount(reqVo);
					if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
						batchOrder.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
					} else {
						batchOrder.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
					}
				} catch (Exception e1) {
					logger.error("远程调用 开户接口异常{}", e1);
					return 0;
				}
				i = i + speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(batchOrder);
				//TODO 发送模板消息（询问需不需要发）
			}
			if (i > 0 && speList.size() == i) {
				order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
			} else {
				order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
			}
			i = speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
		}
		return i;
	}

	@Override
	public int batchSpeAccountRechargeITF(String orderId, User user, String commitStat) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		if ((order != null && BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat()))) {
			
			order.setOrderStat(BatchOrderStat.BatchOrderStat_30.getCode());
			order.setUpdateUser(user.getId().toString());
			order.setUpdateTime(System.currentTimeMillis());
			speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order); // 修改订单状态
		}	
			List<SpeAccountBatchOrderList> speList = getBatchOrderList(orderId, commitStat);
			int i = 0 ;
			/*if (speList != null && speList.size() > 0) {
				for (SpeAccountBatchOrderList batchOrder : speList) {
					AccountOpenReqVo reqVo = new AccountOpenReqVo();
					reqVo.setUserName(batchOrder.getUserName());
					reqVo.setMobilePhone(batchOrder.getPhoneNo());
					reqVo.setIcardNo(batchOrder.getUserCardNo());
					reqVo.setCompanyId(batchOrder.getCompanyId());
					reqVo.setTransId(TransCode.CW80.getCode());
					reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
					reqVo.setUserId(batchOrder.getCompanyId());
					Set<String> bIds = new TreeSet<>();
					String[] str = order.getBizType().split(",");
					for (String s : str) {
						bIds.add(s);
					}
					reqVo.setbIds(bIds);
					reqVo.setUserType(order.getAccountType());
					reqVo.setDmsRelatedKey(batchOrder.getOrderListId());
					reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
					try {
						BaseResult result = accountManageFacade.createAccount(reqVo);
						if (!StringUtil.isNullOrEmpty(result) && result.getCode().equals(Constants.SUCCESS_CODE.toString())) {
							batchOrder.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
						} else {
							batchOrder.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
						}
					} catch (Exception e1) {
						logger.error("远程调用 开户接口异常{}", e1);
						return 0;
					}
					i = i + speAccountBatchOrderListMapper.updateSpeAccountBatchOrderList(batchOrder);
					//TODO 发送模板消息（询问需不需要发）
				}
				if (i > 0 && speList.size() == i) {
					order.setOrderStat(BatchOrderStat.BatchOrderStat_00.getCode());
				} else {
					order.setOrderStat(BatchOrderStat.BatchOrderStat_99.getCode());
				}
				i = speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
			}
			return i;*/
			return 0;
	}

	@Override
	public int deleteOpenAccountCommit(String orderId, User user) {
		SpeAccountBatchOrder order = speAccountBatchOrderMapper.getSpeAccountBatchOrderById(orderId);
		order.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
		order.setUpdateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId());
		List<SpeAccountBatchOrderList> orderList = speAccountBatchOrderListMapper.getSpeAccountBatchOrderListByOrderId(orderId);
		int i = speAccountBatchOrderMapper.updateSpeAccountBatchOrder(order);
		if (i > 0) {
			for (SpeAccountBatchOrderList list : orderList) {
				list.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
				list.setUpdateTime(System.currentTimeMillis());
				list.setUpdateUser(user.getId());
			}
			int j = speAccountBatchOrderListMapper.updateSpeAccountBatchOrderListByList(orderList);
			if (i > 0 && j == orderList.size()) {
				return 1;
			}
		}
		return 0;
	}

}
