package com.cn.thinkx.ecom.goodsmanage.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.ApiSpuService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsMarketType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsProductType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsSkuSyncType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.goodsmanage.service.GoodsManageService;
import com.cn.thinkx.ecom.solr.api.service.SolrGoodsApiService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("goodsManageService")
public class GoodsManageServiceImpl implements GoodsManageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("apiSpuService")
	private ApiSpuService apiSpuService;

	@Autowired
	@Qualifier("goodsService")
	private GoodsService goodsService;

	@Autowired
	@Qualifier("goodsProductService")
	private GoodsProductService goodsProductService;

//	@Autowired
//	private SolrGoodsApiService solrGoodsApiService;

	@Override
	public PageInfo<ApiSpu> getApiSpuPage(int startNum, int pageSize, ApiSpu entity) {
		PageHelper.startPage(startNum, pageSize);
		List<ApiSpu> list = null;
		try {
			list = apiSpuService.getList(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<ApiSpu> page = new PageInfo<ApiSpu>(list);
		page.getList().stream().filter(apiSpu -> {
			apiSpu.setEcomType(GoodsEcomCodeType.findByCode(apiSpu.getEcomCode()).getValue());
			apiSpu.setSkuSyncType(GoodsSkuSyncType.findByCode(apiSpu.getSkuSyncStat()).getValue());
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public PageInfo<Goods> getGoodsPage(int startNum, int pageSize, Goods entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Goods> list = null;
		try {
			list = goodsService.getGoodsList(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<Goods> page = new PageInfo<Goods>(list);
		page.getList().stream().filter(goods -> {
			goods.setEcomType(GoodsEcomCodeType.findByCode(goods.getEcomCode()).getValue());
			goods.setMarketType(GoodsMarketType.findByCode(goods.getMarketEnable()).getValue());
			if (goods.getGoodsPrice() != null) {
				goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
			}
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public BaseResult<Object> updateGoods(Goods entity) {
		try {
			Goods goods = goodsService.selectByPrimaryKey(entity.getGoodsId());
			goods.setGoodsName(entity.getGoodsName());
			goods.setMarketEnable(entity.getMarketEnable());
			int i = goodsService.updateByPrimaryKey(goods);
			if (i > 0) {
				//修改索引库中的商品信息
//				List<Goods> goodsList = goodsService.getGoodsSolrByGoodsId(entity.getGoodsId());
//				for (Goods goods2 : goodsList) {
//					if("1".equals(entity.getMarketEnable())){
//						goods.setPayRate(goodsService.getGoodsPayRateByGoodsId(goods.getGoodsId()));
//						solrGoodsApiService.addGoodsIndex(goods2);
//					}else{
//						solrGoodsApiService.deleteGoodsIndex(goods2.getGoodsId());
//					}
//				}
			}
			return ResultsUtil.success();
		} catch (Exception e) {
			logger.error("###修改商品信息出错（商品上下架）", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}

	@Override
	public PageInfo<GoodsProduct> getGoodsProductPage(int startNum, int pageSize, String goodsId) {
		PageHelper.startPage(startNum, pageSize);
		List<GoodsProduct> list = null;
		try {
			list = goodsProductService.getGoodsProductListByGoodsId(goodsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<GoodsProduct> page = new PageInfo<GoodsProduct>(list);
		page.getList().stream().filter(p -> {
			p.setEcomType(GoodsEcomCodeType.findByCode(p.getEcomCode()).getValue());
			p.setProductEnable(GoodsProductType.findByCode(p.getProductEnable()).getValue());
			if (p.getGoodsPrice() != null) {
				p.setGoodsPrice(NumberUtils.RMBCentToYuan(p.getGoodsPrice()));
			}
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public BaseResult<Object> updateGoodsProduct(GoodsProduct entity) {
		BaseResult<Object> result = null;
		try {
			entity.setGoodsPrice(NumberUtils.RMBYuanToCent(entity.getGoodsPrice()));
			int j = goodsProductService.updateByPrimaryKey(entity);
			if (j != 1) {
				result = ResultsUtil.error(ExceptionEnum.ERROR_CODE, "修改商品SKu信息出错");
			} 
//			else {
//				if (!StringUtil.isNullOrEmpty(entity.getGoodsId()))
//					return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
//
//				List<Goods> goodsList = goodsService.getGoodsSolrByGoodsId(entity.getGoodsId());
//				for (Goods goods2 : goodsList) {
//					solrGoodsApiService.addGoodsIndex(goods2);
//				}
//			}
			result = ResultsUtil.success();
		} catch (Exception e) {
			logger.error("###修改商品SKu信息出错", e);
			result = ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		return result;
	}

}
