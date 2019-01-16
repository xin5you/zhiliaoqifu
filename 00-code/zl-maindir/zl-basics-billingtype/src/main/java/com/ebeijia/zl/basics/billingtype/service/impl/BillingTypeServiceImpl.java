package com.ebeijia.zl.basics.billingtype.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.billingtype.mapper.BillingTypeMapper;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import redis.clients.jedis.JedisCluster;

/**
 *
 * 企业员工在平台账户类型 Service 实现类
 *
 * @Date 2018-12-18
 */
@Service
public class BillingTypeServiceImpl extends ServiceImpl<BillingTypeMapper, BillingType> implements BillingTypeService{

	@Autowired
	private BillingTypeMapper billingTypeMapper;

	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public BillingType getBillingTypeInfById(String bId) {
		return billingTypeMapper.getBillingTypeInfById(bId);
	}

	@Override
	public BillingType getBillingTypeInfByName(String name) {
		return billingTypeMapper.getBillingTypeInfByName(name);
	}

	@Override
	public List<BillingType> getBillingTypeInfList(BillingType billingTypeInf) {
		return billingTypeMapper.getBillingTypeInfList(billingTypeInf);
	}

	@Override
	public PageInfo<BillingType> getBillingTypeInfListPage(int startNum, int pageSize, BillingType billingTypeInf) {
		PageHelper.startPage(startNum, pageSize);
		List<BillingType> list = billingTypeMapper.getBillingTypeInfList(billingTypeInf);
		PageInfo<BillingType> page = new PageInfo<BillingType>(list);
		return page;
	}

	@Override
	public int updateBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.updateBillingTypeInf(billingTypeInf);
	}

	@Override
	public int deleteBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.deleteBillingTypeInf(billingTypeInf);
	}

	@Override
	public int insertBillingTypeInf(BillingType billingTypeInf) {
		return billingTypeMapper.insertBillingTypeInf(billingTypeInf);
	}

	public List<BillingType> getBillingTypeListForRedis() {
		Map<String, String> m = jedisCluster.hgetAll(RedisConstants.REDIS_HASH_TABLE_TB_BILLING_TYPE);
		BillingType billingType=null;
		List list = new ArrayList();
		for (String key : m.keySet()) {
			billingType=JSONObject.parseObject(m.get(key),BillingType.class);
			list.add(billingType);
		}
		m.clear();
		return  list;
	}

	public BillingType getBillingTypeForRedisByBId(String bId){
		String m = jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BILLING_TYPE,bId);
		return JSONObject.parseObject(m,BillingType.class);
	}

}
