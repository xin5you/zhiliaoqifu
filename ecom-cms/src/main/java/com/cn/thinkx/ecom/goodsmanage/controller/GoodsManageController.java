package com.cn.thinkx.ecom.goodsmanage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.ApiSpuService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsMarketType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsSkuSyncType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.goodsmanage.service.GoodsManageService;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXGoodsService;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemTO;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "ecom/goodsManage")
public class GoodsManageController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("yxOpenApiService")
	private YXOpenApiService yxOpenApiService;
	
	@Autowired
	@Qualifier("yxGoodsService")
	private YXGoodsService yxGoodsService;

	@Autowired
	@Qualifier("apiSpuService")
	private ApiSpuService apiSpuService;
	
	@Autowired
	@Qualifier("goodsManageService")
	private GoodsManageService goodsManageService;
	
	@Autowired
	@Qualifier("goodsService")
	private GoodsService goodsService;
	
	@Autowired
	@Qualifier("goodsProductService")
	private GoodsProductService goodsProductService;

	public ModelAndView intoGoodsInfo() {
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	/**
	 * 显示商品spu列表
	 * @param req
	 * @param apiSpu
	 * @return
	 */
	@GetMapping(value = "/listGoodsSpuInfo")
	public ModelAndView listGoodsSpuInfo(HttpServletRequest req, ApiSpu apiSpu) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsSpuInfo");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if(StringUtil.isNullOrEmpty(apiSpu.getEcomCode())){
				apiSpu.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());//
			}
			PageInfo<ApiSpu> pageList = goodsManageService.getApiSpuPage(startNum, pageSize, apiSpu);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("apiSpu", apiSpu);
			mv.addObject("sycTypeList",GoodsSkuSyncType.values());
		} catch (Exception e) {
			logger.error("## 查询商城列表信息出错", e);
		}
		return mv;
	}
	
	@PostMapping(value = "/listGoodsSpuInfo")
	public ModelAndView listGoodsSpuInfo(HttpServletRequest req,HttpServletResponse resp, ApiSpu apiSpu) {
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsSpuInfo");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<ApiSpu> pageList = goodsManageService.getApiSpuPage(startNum, pageSize, apiSpu);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("apiSpu", apiSpu);
			mv.addObject("sycTypeList",GoodsSkuSyncType.values());
		} catch (Exception e) {
			logger.error("## 查询商城列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 同步商品spu
	 * @return
	 */
	@GetMapping("/commitSyncGoodsInfo")
	public BaseResult<Object> commitSyncGoodsInfo(HttpServletRequest req) {
		
		String ecomCode = req.getParameter("ecomCode");
		ApiSpu apiSpu;
		List<ApiSpu> list = new ArrayList<ApiSpu>();
		if(GoodsEcomCodeType.ECOM01.getCode().equals(ecomCode)){
			JsonResult result = yxOpenApiService.handleGetIds();
			if ("200".equals(result.getCode())) {
				JSONArray obj = (JSONArray)result.getResult();
				Object[] spus =obj.toArray();
				for(Object sup:spus){
					apiSpu = new ApiSpu();
					apiSpu.setEcomCode(ecomCode);
					apiSpu.setSpuCode(String.valueOf(sup));
					list.add(apiSpu);
				}
			}
		}
		try {
			
			apiSpuService.insertApiSpuList(list);
			return ResultsUtil.success(ecomCode);
		} catch (Exception e) {
			logger.error("## 同步商品出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}
	
	/**
	 * 添加商品
	 * @return
	 */
	@GetMapping("/commitGoodsInfo")
	public BaseResult<Object> commitGoodsInfo(HttpServletRequest req) {
		BaseResult<Object> returnResult = null;
		try {
			String spuids = req.getParameter("ids");
			String ecomCode = req.getParameter("ecomCode");
			if (!StringUtil.isNullOrEmpty(spuids)) {
				List<String> idItems = new ArrayList<>();
				
				for(String spuId : spuids.split(",")){
					idItems.add(spuId);
				}
				List<ApiSpu>list = apiSpuService.getApiByIdItems(idItems);
				if(GoodsEcomCodeType.ECOM01.getCode().equals(ecomCode)){
					JsonResult result ;
					JSONArray result1;
					JSONObject resultObj;
					APIItemTO item;
					String baseResult;
					for (ApiSpu apiSpu : list) {
						result = yxOpenApiService.handleGetItemsById(apiSpu.getSpuCode());
						if("200".equals(result.getCode())){
							result1 = (JSONArray)result.getResult();
							if(result1!= null && result1.size() > 0){
								resultObj = (JSONObject)result1.get(0);
								item = JSONArray.parseObject(JSONObject.toJSONString(resultObj), APIItemTO.class);
								if(!StringUtil.isNullOrEmpty(item)){
									baseResult = yxGoodsService.insertGoods(item);	
									returnResult = JSONObject.parseObject(baseResult, BaseResult.class);
									if(ExceptionEnum.SUCCESS_CODE.equals(returnResult.getCode())){
										apiSpu.setSkuSyncStat("1");
										int i = apiSpuService.updateByPrimaryKey(apiSpu);
										if(i!=1){
											
										}
										returnResult = ResultsUtil.success(ecomCode);
									} else{
										return returnResult;
									}
								}
							}
						}else{
							returnResult =  ResultsUtil.error(result.getCode(), result.getMessage());
						}
					}
				}
			} else {
				returnResult =  ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			
		} catch (Exception e) {
			logger.error("## 同步商品出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return returnResult;
	}
	
	/**
	 * 显示商品信息列表
	 * @return
	 */
	@GetMapping("/listGoodsInfo")
	public ModelAndView listGoodsInfo(HttpServletRequest req,Goods goods){
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsInfo");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if(StringUtil.isNullOrEmpty(goods.getEcomCode())){
				goods.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());//
			}
			PageInfo<Goods> pageList = goodsManageService.getGoodsPage(startNum, pageSize, goods);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("marketList", GoodsMarketType.values());
			mv.addObject("goods", goods);
		} catch (Exception e) {
			logger.error("## 查询商品信息列表出错", e);
		}
		
		return mv;
	}
	
	
	@PostMapping("/listGoodsInfo")
	public ModelAndView listGoodsInfo(HttpServletRequest req,HttpServletResponse resp,Goods goods){
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsInfo");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
//			if(StringUtil.isNullOrEmpty(goods.getEcomCode())){
//				goods.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());//
//			}
			PageInfo<Goods> pageList = goodsManageService.getGoodsPage(startNum, pageSize, goods);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("marketList", GoodsMarketType.values());
			mv.addObject("goods", goods);
		} catch (Exception e) {
			logger.error("## 查询商品信息列表出错", e);
		}
		
		return mv;
	}
	
	@PostMapping("/intoEditGoods")
	public Goods intoEditGoods(HttpServletRequest req){
		
		String goodsId = req.getParameter("goodsId");
		Goods goods = new Goods();
		goods.setGoodsId(goodsId);
		Goods returnGoods = null;
		try {
			returnGoods = goodsService.getGoods(goods);
			returnGoods.setGoodsPrice(NumberUtils.RMBCentToYuan(returnGoods.getGoodsPrice()));
		} catch (Exception e) {
			logger.error("## 查询商品信息出错", e);
		}
		return returnGoods;
	}
	
	
	@PostMapping("/commitEditGoods")
	public BaseResult<Object> commitEditGoods(HttpServletRequest req,Goods goods){
		
		try {
			return goodsManageService.updateGoods(goods);
		} catch (Exception e) {
			logger.error("## 修改商品信息出错", e);
		}
		return null;
	}
	
	@GetMapping("/getGoodsInfo")
	public ModelAndView getGoodsInfo(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("goodsManage/goodsInfo");
		
		JsonResult result = null;
		
		String spuCode = req.getParameter("spuCode");
		String ecomCode = req.getParameter("ecomCode");
		
		if(Constants.GoodsEcomCodeType.ECOM01.getCode().equals(ecomCode)){
			result = yxOpenApiService.handleGetItemsById(spuCode);
		}
		
		mv.addObject("result", result);
		return mv;
	}
	
	@GetMapping("/intoEditGoodsProductPrice")
	public ModelAndView intoEditGoodsProductPrice(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsProduct");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			String goodsId = req.getParameter("goodsId");
			GoodsProduct pro = new GoodsProduct();
			pro.setGoodsId(goodsId);
			PageInfo<GoodsProduct> pageList = goodsManageService.getGoodsProductPage(startNum, pageSize, goodsId);
			mv.addObject("pageInfo", pageList);
			mv.addObject("pro", pro);
		} catch (Exception e) {
			logger.error("## 查询商品信息列表出错", e);
		}
		return mv;
	}
	
	@PostMapping("/intoEditGoodsProductPrice")
	public ModelAndView intoEditGoodsProductPrice(HttpServletRequest req,HttpServletResponse resp ,GoodsProduct pro){
		ModelAndView mv = new ModelAndView("goodsManage/listGoodsProduct");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<GoodsProduct> pageList = goodsManageService.getGoodsProductPage(startNum, pageSize, pro.getGoodsId());
			mv.addObject("pageInfo", pageList);
			mv.addObject("pro", pro);
		} catch (Exception e) {
			logger.error("## 查询商品信息列表出错", e);
		}
		return mv;
	}
	
	@PostMapping("/intoEditGoodsProduct")
	public GoodsProduct intoEditGoodsProduct(HttpServletRequest req){
		String productId = req.getParameter("productId");
		GoodsProduct pro = null;
		try{
			pro = goodsProductService.getGoodsProductByPrimaryKey(productId);
			pro.setGoodsPrice(NumberUtils.RMBCentToYuan(pro.getGoodsPrice()));
		} catch (Exception e) {
			logger.error("## into编辑商品Sku信息出错", e);
		}
		return pro;
	}
	
	@PostMapping("/commitEditGoodsProduct")
	public BaseResult<Object> commitEditGoodsProduct(HttpServletRequest req,GoodsProduct pro){
		try {
			return goodsManageService.updateGoodsProduct(pro);
		} catch (Exception e) {
			logger.error("## 修改商品信息出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}
	public static void main(String[] args) {
		String [] str = {"1","2"};
		System.out.println(str.toString());
		
	}
}
