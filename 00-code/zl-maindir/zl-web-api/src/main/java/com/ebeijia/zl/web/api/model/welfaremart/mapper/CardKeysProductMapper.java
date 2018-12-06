package com.ebeijia.zl.web.api.model.welfaremart.mapper;

import java.util.List;

import com.ebeijia.zl.web.api.model.welfaremart.model.CardKeysProduct;

public interface CardKeysProductMapper {
	
	CardKeysProduct getCardKeysProductByCode(CardKeysProduct cardKeysProduct);
	
	List<CardKeysProduct> getCardKeysProductByType(String productCode);
	
	CardKeysProduct getCardKeysProductByCard(CardKeysProduct cardKeysProduct);
	
	int updateCardKeysProduct(CardKeysProduct cardKeysProduct);
	
}
