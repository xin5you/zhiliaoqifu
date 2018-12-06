package com.ebeijia.zl.web.api.model.welfaremart.service;

import com.ebeijia.zl.web.api.model.welfaremart.model.CardKeysProduct;

public interface CardKeysProductService {
	
	CardKeysProduct getCardKeysProductByCode(CardKeysProduct cardKeysProduct);
	
	CardKeysProduct getCardKeysProductByCard(CardKeysProduct cardKeysProduct);
	
	int updateCardKeysProduct(CardKeysProduct cardKeysProduct);
	
}
