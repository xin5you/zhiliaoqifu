package com.ebeijia.zl.shop.dao.goods.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class GoodsProduct extends Model<GoodsProduct> {
	
	private static final long serialVersionUID = -3209543443585542837L;
	
	private String productId;
	private String goodsId;
	private String spuCode;
	private String skuCode;
	private String ecomCode;
	private Long isStore;
	private Long enableStore;
	private String goodsPrice;
	private String goodsCost;
	private String mktprice;
	private String pageTitle;
	private String metaDescription;
	private String picurl;
	private String productEnable;
	
	/**货品规格值列表*/
	private List<String> specs;
	
	private String goodsName;
	private String marketType;
	private String marketEnable;
	private String ecomType;
}
