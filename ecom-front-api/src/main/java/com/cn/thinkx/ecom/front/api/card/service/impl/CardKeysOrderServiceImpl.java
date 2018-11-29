package com.cn.thinkx.ecom.front.api.card.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.front.api.card.mapper.CardKeysOrderMapper;
import com.cn.thinkx.ecom.front.api.card.service.CardKeysOrderService;

@Service("cardKeysOrderService")
public class CardKeysOrderServiceImpl implements CardKeysOrderService {

	@Autowired
	private CardKeysOrderMapper cardKeysOrderMapper;

	@Override
	public int getCardKeysOrderCount(String userId) {
		int count = cardKeysOrderMapper.getCardKeysOrderCount(userId);
		return count;
	}
}
