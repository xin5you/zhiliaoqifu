package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.mapper.ApiSpuMapper;
import com.cn.thinkx.ecom.basics.goods.service.ApiSpuService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.StringUtil;

@Service("apiSpuService")
public class ApiSpuServiceImpl extends BaseServiceImpl<ApiSpu> implements ApiSpuService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	

	@Autowired
	private ApiSpuMapper apiSpuMapper;
	
	/**
	 * 保存渠道的Spu字符串信息
	 * @param spuList 集合对象
	 * @throws Exception
	 */
	public void insertApiSpuList(List<ApiSpu> spuList) throws Exception{
		logger.info("insertApiSpuList spuList={}",spuList);
		for(ApiSpu apiSpu:spuList){
			this.insertApiSpu(apiSpu);
		}
	}

	/**
	 *  获取渠道的spu信息
	 * @param ecomCode
	 * @param spuCode
	 * @return ApiSpu
	 */
	public ApiSpu  getApiSpuByEcomCodeAndSpu(String ecomCode,String spuCode) throws Exception{
		return apiSpuMapper.getApiSpuByEcomCodeAndSpu(ecomCode,spuCode);
	}
	
	/**
	 * 保存渠道spu信息
	 * @return 0:未成功 1：成功
	 * @throws Exception
	 */
	public int insertApiSpu(ApiSpu apiSpu)throws Exception{
		int oper=0;
		if(apiSpu==null  || StringUtil.isNullOrEmpty(apiSpu.getEcomCode()) || StringUtil.isNullOrEmpty(apiSpu.getSpuCode())){
			oper=0;
		}else{
			
			//保存渠道SPU信息，先查询当前渠道的SPU信息是否已经存在
			ApiSpu entity=apiSpuMapper.getApiSpuByEcomCodeAndSpu(apiSpu.getEcomCode(), apiSpu.getSpuCode());
			if(entity==null){
				oper=apiSpuMapper.insert(apiSpu);
			}else{
				oper=1;
			}
		}
		return oper;
		
	}

	@Override
	public List<ApiSpu> getApiByIdItems(List<String> idItems) {
		return apiSpuMapper.getApiByIdItems(idItems);
	}
}
