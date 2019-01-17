package com.ebeijia.zl.shop.dao.goods.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 商品
 * 
 * @author zhupan
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Goods extends Model<Goods> {

	private static final long serialVersionUID = 3028855457630002260L;
	private String goodsId;
	private String goodsName;
	private String spuCode;
	private String ecomCode;
	private String catId;
	private String goodsType;
	private String unit;
	private String weight;
	private String defaultSkuCode;
	private String marketEnable;
	private String brief;
	private String haveParams;
	private String haveSpec;
	private String isDisabled;
	private Long ponumber;
	private Long goodsSord;
	private String goodsWeight;
	private String grade;
	private String isHot;
	private String goodsImg;
	private String bId;
	private String skuCode;
	private String dataStat;
	private String productId; //货品Id
	private String pageTitle; //货品名称
	private String ecomType;
	private String marketType;
	private String catName; //分类名称
	private String picUrl;//图片
	private String goodsPrice;//商品价格
	private String specName;
	private String specValue;
	private String parentCatId;
	private String ecomName;
	private String parentCatName;
	private Integer buyCount;	//商品的购买数
	private Integer viewCount; //浏览量

}
