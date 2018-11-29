package com.cn.thinkx.ecom.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.FrontApp;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsSpec;
import com.cn.thinkx.ecom.basics.goods.domain.SpecValues;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsProductMapper;
import com.cn.thinkx.ecom.basics.goods.mapper.GoodsSpecMapper;
import com.cn.thinkx.ecom.basics.goods.mapper.SpecValuesMapper;
import com.cn.thinkx.ecom.basics.goods.service.ApiSpuService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.goods.service.GoodsSpecService;
import com.cn.thinkx.ecom.basics.goods.service.SpecValuesService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrontApp.class)// 指定spring-boot的启动类
public class GoodsSpecTest {

	@Autowired
	private ApiSpuService apiSpuService;
	
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	@Autowired
	private GoodsProductService goodsProductService;
	
	@Autowired
	private SpecValuesService specValuesService;
	
    @Test
    public void find() throws Exception  {
    	
    	String goodsId="100041";
    	
    	//商品规格
    	//List<GoodsSpec> gSpecList=goodsSpecService.getGoodsSpecByGoodsId(goodsId);
    	
    	//商品规格分组
    	//Map<String, List<GoodsSpec>> specMaps=gSpecList.stream().collect(Collectors.groupingBy(t->t.getSpecId()));
    	
    	//商品下所有的货品
    	List<GoodsProduct> productList=goodsProductService.getProductlistByGoodsId(goodsId);
    
    	//少年派的规格值表
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
    	
    	System.out.println(JSONObject.toJSON(productList));
    }
	
}
