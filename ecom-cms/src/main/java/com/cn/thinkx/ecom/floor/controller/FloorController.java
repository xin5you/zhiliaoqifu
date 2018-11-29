package com.cn.thinkx.ecom.floor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.FloorGoods;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.exception.BizHandlerException;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.floor.service.FloorInfService;
import com.cn.thinkx.ecom.system.domain.User;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "floor")
public class FloorController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FloorInfService floorService;

	/**
	 * 电商楼层列表查询
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listFloors")
	public ModelAndView listFloors(HttpServletRequest req, Floor floor) {
		ModelAndView mv = new ModelAndView("floor/listFloors");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Floor> pageList = floorService.getFloorListPage(startNum, pageSize, floor);
			mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
			mv.addObject("isDisplayList", Constants.FloorIsDisplay.values());
			mv.addObject("floorTypeList", Constants.FloorType.values());
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询电商楼层商品展示表时出错", e);
		}
		return mv;
	}

	/**
	 * 电商楼层列表查询
	 * 
	 * @param req
	 * @retrun
	 */
	@PostMapping(value = "/listFloors")
	public ModelAndView listFloors(HttpServletRequest req, HttpServletResponse resp, Floor floor) {
		ModelAndView mv = new ModelAndView("floor/listFloors");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<Floor> pageList = floorService.getFloorListPage(startNum, pageSize, floor);
			mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
			mv.addObject("isDisplayList", Constants.FloorIsDisplay.values());
			mv.addObject("floorTypeList", Constants.FloorType.values());
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 查询电商楼层商品展示表时出错", e);
		}
		return mv;
	}

	/**
	 * 根据主键查询电商楼层列表
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/getFloor")
	public Floor getFloor(HttpServletRequest req) {
		String floorId = req.getParameter("floorId");
		Floor floor = null;
		try {
			floor = floorService.selectByPrimaryKey(floorId);
		} catch (Exception e) {
			logger.error("## 查询主键为[" + floorId + "]的商城信息出错", e);
		}
		return floor;
	}

	/**
	 * 新增电商楼层
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/addFloor")
	public BaseResult<Object> addFloor(HttpServletRequest req, Floor floor) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			floor.setCreateUser(user.getId().toString());
			floor.setUpdateUser(user.getId().toString());
			if (floor.getType().equals(Constants.FloorType.ID00.getCode())) {
				floor.setEcomCode("0");
			}
			if (floorService.insert(floor) > 0) {
				return ResultsUtil.success();
			} else {
				return ResultsUtil.error(ExceptionEnum.floorNews.FN01.getCode(), ExceptionEnum.floorNews.FN01.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("## 新增电商楼层商品出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("## 新增电商楼层商品出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}

	}

	/**
	 * 修改电商楼层
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/editFloor")
	public BaseResult<Object> editFloor(HttpServletRequest req, Floor floor) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Floor fr = floorService.selectByPrimaryKey(floor.getFloorId());
			if (fr != null) {
				fr.setParentId(floor.getParentId());
				fr.setTitle(floor.getTitle());
				fr.setEcomCode(floor.getEcomCode());
				fr.setCatId(floor.getCatId());
				fr.setType(floor.getType());
				fr.setLogo(floor.getLogo());
				fr.setIsDisplay(floor.getIsDisplay());
				fr.setSort(floor.getSort());
				fr.setCatId(floor.getCatId());
				fr.setUpdateUser(user.getId().toString());
				if (floorService.updateByPrimaryKey(fr) > 0) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.floorNews.FN02.getCode(),
							ExceptionEnum.floorNews.FN02.getMsg());
				}
			} else {
				return ResultsUtil.error(ExceptionEnum.floorNews.FN05.getCode(), ExceptionEnum.floorNews.FN05.getMsg());
			}
		} catch (BizHandlerException e) {
			logger.error("##编辑电商楼层商品出错", e.getMessage());
			return ResultsUtil.error(e.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("##编辑电商楼层商品出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);

		}

	}

	/**
	 * 刪除电商楼层
	 * 
	 * @param req
	 * @param ecomName
	 * @return
	 * 
	 */
	@PostMapping(value = "/deleteFloor")
	public BaseResult<Object> deleteFloor(HttpServletRequest req) {
		return floorService.deleteFloor(req);
	}

	/**
	 * 进入楼层对应的商品列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "intoFloorEgoodsList")
	public ModelAndView intoFloorEgoodsList(HttpServletRequest req, HttpServletResponse resp, FloorGoods entity) {
		ModelAndView mv = new ModelAndView("floor/listFloorEgoods");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String floorId = req.getParameter("floorId");
		if (!StringUtil.isNullOrEmpty(entity.getFloorId())) {
			floorId = entity.getFloorId();
		} else {
			entity.setFloorId(floorId);
		}
		PageInfo<FloorGoods> pageList = null;
		try {
			Floor floor = floorService.selectByPrimaryKey(entity.getFloorId());
			if (Constants.FloorType.ID01.getCode().equals(floor.getType())) {
				entity.setEcomCode(floor.getEcomCode());
			}
			pageList = floorService.getFloorGoodsListPage(startNum, pageSize, entity, "1");
		} catch (Exception e) {
			logger.error("## 查询商品信息出错", e);
		}
		mv.addObject("entity", entity);
		mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
		mv.addObject("pageInfo", pageList);
		mv.addObject("floorId", floorId);
		return mv;
	}

	/**
	 * 进入楼层对应的商品列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@GetMapping(value = "intoFloorEgoodsList")
	public ModelAndView intoFloorEgoodsList(HttpServletRequest req, FloorGoods entity) {
		ModelAndView mv = new ModelAndView("floor/listFloorEgoods");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String floorId = req.getParameter("floorId");
		if (!StringUtil.isNullOrEmpty(entity.getFloorId())) {
			floorId = entity.getFloorId();
		} else {
			entity.setFloorId(floorId);
		}
		PageInfo<FloorGoods> pageList = null;
		try {
			Floor floor = floorService.selectByPrimaryKey(entity.getFloorId());
			if (Constants.FloorType.ID01.getCode().equals(floor.getType())) {
				entity.setEcomCode(floor.getEcomCode());
			}
			pageList = floorService.getFloorGoodsListPage(startNum, pageSize, entity, "1");
		} catch (Exception e) {
			logger.error("## 查询商品信息出错", e);
		}
		mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
		mv.addObject("entity", entity);
		mv.addObject("pageInfo", pageList);
		mv.addObject("floorId", floorId);
		return mv;
	}

	/**
	 * 进入楼层没有选中的商品页面
	 * 
	 * @param req
	 * @param resp
	 * @param entity
	 * @return
	 */
	@PostMapping(value = "intoEgoodsList")
	public ModelAndView intoEgoodsList(HttpServletRequest req, HttpServletResponse resp, FloorGoods entity) {
		ModelAndView mv = new ModelAndView("floor/listEgoods");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String floorId = req.getParameter("floorId");
		if (!StringUtil.isNullOrEmpty(entity.getFloorId())) {
			floorId = entity.getFloorId();
		} else {
			entity.setFloorId(floorId);
		}
		PageInfo<FloorGoods> pageList = null;
		try {
			if(!StringUtil.isNullOrEmpty(entity.getFloorId())){
				Floor floor = floorService.selectByPrimaryKey(entity.getFloorId());
				if (Constants.FloorType.ID01.getCode().equals(floor.getType())) {
					entity.setEcomCode(floor.getEcomCode());
				}
				pageList = floorService.getFloorGoodsListPage(startNum, pageSize, entity, "0");
			}
		} catch (Exception e) {
			logger.error("## 查询商品信息出错", e);
		}
		mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
		mv.addObject("entity", entity);
		mv.addObject("pageInfo", pageList);
		mv.addObject("floorId", floorId);
		return mv;
	}

	/**
	 * 进入楼层没有选中的商品页面
	 * 
	 * @param req
	 * @param resp
	 * @param entity
	 * @return
	 */
	@GetMapping(value = "intoEgoodsList")
	public ModelAndView intoEgoodsList(HttpServletRequest req, FloorGoods entity) {
		ModelAndView mv = new ModelAndView("floor/listEgoods");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String floorId = req.getParameter("floorId");
		if (!StringUtil.isNullOrEmpty(entity.getFloorId())) {
			floorId = entity.getFloorId();
		} else {
			entity.setFloorId(floorId);
		}
		PageInfo<FloorGoods> pageList = null;
		try {
			if(!StringUtil.isNullOrEmpty(entity.getFloorId())){
				Floor floor = floorService.selectByPrimaryKey(entity.getFloorId());
				if (Constants.FloorType.ID01.getCode().equals(floor.getType())) {
					entity.setEcomCode(floor.getEcomCode());
				}
				pageList = floorService.getFloorGoodsListPage(startNum, pageSize, entity, "0");
			}
		} catch (Exception e) {
			logger.error("## 查询商品信息出错", e);
		}
		mv.addObject("channelList", Constants.GoodsEcomCodeType.values());
		mv.addObject("entity", entity);
		mv.addObject("pageInfo", pageList);
		mv.addObject("floorId", floorId);
		return mv;
	}

	/**
	 * 楼层添加商品提交
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "addFloorEgoods")
	public BaseResult<Object> addFloorEgoods(HttpServletRequest req) {
		try {
			String floorId = req.getParameter("floorId");
			String ids = req.getParameter("ids");
			if (StringUtils.isEmpty(ids)) {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				if (floorService.addFloorEgoods(floorId, ids, req)) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.floorNews.FN06.getCode(),
							ExceptionEnum.floorNews.FN06.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error("## 添加电商楼层商品出错", e);
			return ResultsUtil.error(ExceptionEnum.floorNews.FN06.getCode(), ExceptionEnum.floorNews.FN06.getMsg());
		}
	}

	/**
	 * 楼层添加商品提交
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "addFloorEgoods")
	public BaseResult<Object> addFloorEgoods(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String floorId = req.getParameter("floorId");
			String ids = req.getParameter("ids");
			if (StringUtils.isEmpty(ids)) {
				return ResultsUtil.error(ExceptionEnum.userNews.UN10.getCode(), ExceptionEnum.userNews.UN10.getMsg());
			} else {
				if (floorService.addFloorEgoods(floorId, ids, req)) {
					return ResultsUtil.success();
				} else {
					return ResultsUtil.error(ExceptionEnum.floorNews.FN06.getCode(),
							ExceptionEnum.floorNews.FN06.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error("## 添加电商楼层商品出错", e);
			return ResultsUtil.error(ExceptionEnum.floorNews.FN06.getCode(), ExceptionEnum.floorNews.FN06.getMsg());
		}
	}

	/**
	 * 删除楼层商品信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "deleteFloorGoods")
	public BaseResult<Object> deleteFloorGoods(HttpServletRequest req) {
		return floorService.deleteFloorGoods(req);
	}

}
