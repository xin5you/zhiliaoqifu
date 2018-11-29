package com.cn.thinkx.ecom.front.api.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.basics.goods.domain.SpecValues;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsSpecService;
import com.cn.thinkx.ecom.basics.goods.service.SpecValuesService;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.base.controller.EcomBaseController;
import com.cn.thinkx.ecom.front.api.goods.domain.GoodsDetail;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemDetailTO;
import com.cn.thinkx.ecom.you163.api.vo.goods.APIItemTO;

@RestController
@RequestMapping(value = "ecom/goods")
public class GoodsController extends EcomBaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Qualifier("yxOpenApiService")
	@Autowired
	private YXOpenApiService yxOpenApiService;
	
	@Qualifier("goodsService")
	@Autowired
	private GoodsService goodsService;
	
	
	@Qualifier("goodsSpecService")
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	@Qualifier("goodsProductService")
	@Autowired
	private GoodsProductService goodsProductService;
	
	@Qualifier("specValuesService")
	@Autowired
	private SpecValuesService specValuesService;
	
	@Qualifier("cartService")
	@Autowired
	private CartService cartService;

	/**
	 * 渠道商城商品明细
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping("/intoGoodsInfo")
	public ModelAndView intoGoodsInfo(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		String goodsId = req.getParameter("goodsId");
		String ecomCode = req.getParameter("ecomCode");
		JsonResult result = null;
		if(Constants.GoodsEcomCodeType.ECOM01.getCode().equals(ecomCode)){
			result = yxOpenApiService.handleGetItemsById(goodsId);
		}
		JSONArray result1 = (JSONArray)result.getResult();
		JSONObject resultObj = (JSONObject)result1.get(0);
		APIItemTO item = JSONArray.parseObject(JSONObject.toJSONString(resultObj), APIItemTO.class);
		mv.addObject("item", item);
		return mv;
	}
	
	
	/**
	 * 商品明细
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping("/detail")
	public ModelAndView goodsDetail(HttpServletRequest req){
		
		ModelAndView mv = new ModelAndView("ecom/goods/goodsDetail");
		
		String goodsId = req.getParameter("goodsId");
		String productId = req.getParameter("productId");
		
		//step1: 查找商品
		Goods goods=null;
		try {
			
			if(StringUtil.isNotEmpty(productId)){
				goods=goodsService.selectGoodsByProductId(productId);
				if(goods !=null){
					goodsId=goods.getGoodsId();
				}
			}else{
				goods = goodsService.selectGoodsAndDefProductByGoodId(goodsId); //包含默认的sku信息
			}
			if(goods !=null){
				goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
			}
		} catch (Exception e) {
			logger.error("detail error=[{}]",e);
			return handleError();
		}
		
	
		//严选商品明细
		try{
			if(Constants.GoodsEcomCodeType.ECOM01.getCode().equals(goods.getEcomCode())){
				JsonResult result = yxOpenApiService.handleGetItemsById(goods.getSpuCode());
				
				if(result==null || ! "200".equals(result.getCode())){
					return handleError();
				}
				JSONArray respResult = (JSONArray)result.getResult();
				JSONObject resultObj = (JSONObject)respResult.get(0);
				
				APIItemTO item = JSONArray.parseObject(JSONObject.toJSONString(resultObj), APIItemTO.class);
				GoodsDetail goodsDetail = new GoodsDetail();
				APIItemDetailTO itemDetail = item.getItemDetail();
				
				List<String> list = new ArrayList<String>();
				
				if(!StringUtil.isNullOrEmpty(itemDetail.getPicUrl1())){
					list.add(itemDetail.getPicUrl1());
				}
				if(!StringUtil.isNullOrEmpty(itemDetail.getPicUrl2())){
					list.add(itemDetail.getPicUrl2());
				}
				if(!StringUtil.isNullOrEmpty(itemDetail.getPicUrl3())){
					list.add(itemDetail.getPicUrl3());
				}
				if(!StringUtil.isNullOrEmpty(itemDetail.getPicUrl4())){
					list.add(itemDetail.getPicUrl4());
				}
				
				if(!StringUtil.isNullOrEmpty(itemDetail.getDetailHtml())){
					goodsDetail.setGoodsDetail(itemDetail.getDetailHtml());
				}
				goodsDetail.setImgStr(list);
				goodsDetail.setAttrList(item.getAttrList());
				mv.addObject("goodsDetail", goodsDetail);
			}
		} catch (Exception e) {
			logger.error("specs error=[{}]",e);
			return handleError();
		}
		
		//step2:商品规格查询
		try{
    	//商品规格
    	List<GoodsSpec> gSpecList=goodsSpecService.getGoodsSpecByGoodsId(goodsId);
    	
    	//商品规格分组
    	Map<String, List<GoodsSpec>> specMaps=gSpecList.stream().collect(Collectors.groupingBy(t->t.getSpecName()));
    	
    	//商品下所有的货品
    	List<GoodsProduct> productList=goodsProductService.getProductlistByGoodsId(goodsId);
    	
    	productList.stream().filter(pro -> {
    		
    		//金额转换
    		pro.setGoodsPrice(NumberUtils.RMBCentToYuan(pro.getGoodsPrice())); 
    		return true;
		}).collect(Collectors.toList());
//    	商品规格值表
    	List<SpecValues> svLists=specValuesService.getGoodsSpecByGoodsId(goodsId);
    	
    	//货品的规格值ID
    	List<String> specs=null;
    	for(GoodsProduct gpro:productList){
    		specs=new ArrayList<>();
    		for(SpecValues sv:svLists){
    			if(gpro.getProductId().equals(sv.getProductId())){
    				specs.add(sv.getSpecValueId());
    			}
    			gpro.setSpecs(specs);
    		}
    	}
    	//购物车数量:
		int cartNum=0;
		
		//查询购物车数量
    	String memberId=StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		if(StringUtil.isNotEmpty(memberId)){
			cartNum=cartService.getCartCountByMemberId(memberId);
		}
		
    	mv.addObject("cartNum", cartNum);
    	mv.addObject("specMaps", specMaps);
    	mv.addObject("productList", productList);
    	mv.addObject("svLists", svLists);
    	
    	//商品信息
    	mv.addObject("goods", goods);
    	
		} catch (Exception e) {
			logger.error("specs error=[{}]",e);
			return handleError();
		}
		return mv;
	}
	
}
