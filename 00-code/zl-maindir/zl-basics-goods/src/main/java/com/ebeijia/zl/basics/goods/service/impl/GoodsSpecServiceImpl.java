package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.GoodsSpec;
import com.ebeijia.zl.basics.goods.mapper.GoodsSpecMapper;
import com.ebeijia.zl.basics.goods.service.GoodsSpecService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
