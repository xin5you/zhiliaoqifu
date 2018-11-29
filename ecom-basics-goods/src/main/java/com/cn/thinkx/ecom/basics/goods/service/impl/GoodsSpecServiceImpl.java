package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsSpecMapper;
import com.cn.thinkx.ecom.basics.goods.service.GoodsSpecService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

/**
 * 商品货品规格对照 serviceImpl
 * @author zhuqiuyou
 *
 */
@Service("goodsSpecService")
public class GoodsSpecServiceImpl extends BaseServiceImpl<GoodsSpec> implements GoodsSpecService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired
	private GoodsSpecMapper goodsSpecMapper;
	
	/**
	 * 查找商品的规格
	 * @param goodsId
	 * @return
	 */
	public List<GoodsSpec> getGoodsSpecByGoodsId(String goodsId){
		return goodsSpecMapper.getGoodsSpecByGoodsId(goodsId);
	}

}
