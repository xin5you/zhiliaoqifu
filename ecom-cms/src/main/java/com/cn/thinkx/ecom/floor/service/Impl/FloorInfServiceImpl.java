package com.cn.thinkx.ecom.floor.service.Impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.FloorGoods;
import com.cn.thinkx.ecom.basics.goods.service.FloorGoodsService;
import com.cn.thinkx.ecom.basics.goods.service.FloorService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.floor.service.FloorInfService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("floorInfService")
public class FloorInfServiceImpl extends BaseServiceImpl<Floor> implements FloorInfService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FloorService floorService;

	@Autowired
	private FloorGoodsService floorGoodsService;

	@Override
	public PageInfo<Floor> getFloorListPage(int startNum, int pageSize, Floor entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Floor> floorList = null;
		try {
			floorList = floorService.getList(entity);
			for (Floor floor : floorList) {
				floor.setType(Constants.FloorType.findByCode(floor.getType()).getValue());
				if ("0".equals(floor.getEcomCode())) {
					floor.setEcomCode("");
				} else {
					floor.setEcomCode(Constants.GoodsEcomCodeType.findByCode(floor.getEcomCode()).getValue());
				}
				floor.setIsDisplay(Constants.FloorIsDisplay.findByCode(floor.getIsDisplay()).getValue());
			}
		} catch (Exception e) {
			logger.error("## 查询电商楼层异常", e);
		}
		PageInfo<Floor> page = new PageInfo<Floor>(floorList);
		return page;
	}

	@Override
	public boolean addFloorEgoods(String floorId, String ids, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			String[] goodsId = ids.split(",");
			for (int i = 0; i < goodsId.length; i++) {
				FloorGoods fg = new FloorGoods();
				fg.setFloorId(floorId);
				fg.setGoodsId(goodsId[i]);
				fg.setUpdateUser(user.getId().toString());
				fg.setCreateUser(user.getId().toString());
				floorGoodsService.insert(fg);
			}
		} catch (Exception e) {
			logger.error("## 楼层添加商品失败", e);
			return false;
		}
		return true;
	}

	@Override
	public PageInfo<FloorGoods> getFloorGoodsListPage(int startNum, int pageSize, FloorGoods entity, String type) {
		PageHelper.startPage(startNum, pageSize);
		List<FloorGoods> floorGoodsList = null;
		try {
			if (StringUtil.isNullOrEmpty(entity.getFloorId())) {
				return null;
			}
			if ("0".equals(type)) {
				floorGoodsList = floorGoodsService.getGoods(entity); // 楼层没有选择的商品
			} else {
				floorGoodsList = floorGoodsService.getFloorGoods(entity); // 该楼层下选择的商品
			}
			for (FloorGoods floorGoods : floorGoodsList) {
				floorGoods.setEcomCode(Constants.GoodsEcomCodeType.findByCode(floorGoods.getEcomCode()).getValue());
				floorGoods.setGoodsPrice(NumberUtils.RMBCentToYuan(floorGoods.getGoodsPrice()));
			}
		} catch (Exception e) {
			logger.error("## 查询楼层添加商品", e);
		}
		PageInfo<FloorGoods> page = new PageInfo<FloorGoods>(floorGoodsList);
		return page;
	}

	@Override
	public BaseResult<Object> deleteFloorGoods(HttpServletRequest req) {
		String floorId = req.getParameter("floorId");
		String goodsId = req.getParameter("goodsId");
		FloorGoods fg = new FloorGoods();
		if (!StringUtil.isNullOrEmpty(floorId) && !StringUtil.isNullOrEmpty(goodsId)) {
			fg.setFloorId(floorId);
			fg.setGoodsId(goodsId);
		}
		try {
			if (floorGoodsService.deleteByFloorIdAndGoodsId(fg) < 0) {
				return ResultsUtil.error(ExceptionEnum.floorNews.FN03.getCode(), ExceptionEnum.floorNews.FN03.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除电商楼层商品出错", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除电商楼层商品出错，id：[{}]", floorId, e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}

	@Override
	public BaseResult<Object> deleteFloor(HttpServletRequest req) {
		String floorId = req.getParameter("floorId");
		try {
			if (floorGoodsService.deleteByPrimaryKey(floorId) < 0) {
				return ResultsUtil.error(ExceptionEnum.floorNews.FN03.getCode(), ExceptionEnum.floorNews.FN03.getMsg());
			}
			if (floorService.deleteByPrimaryKey(floorId) < 0) {
				return ResultsUtil.error(ExceptionEnum.floorNews.FN03.getCode(), ExceptionEnum.floorNews.FN03.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 删除电商楼层出错", e);
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 删除电商楼层出错，id：[{}]", floorId, e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return ResultsUtil.success();
	}

}
