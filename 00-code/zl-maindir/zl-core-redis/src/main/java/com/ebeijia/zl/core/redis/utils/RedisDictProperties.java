package com.ebeijia.zl.core.redis.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisDictProperties {

	private Logger logger = LoggerFactory.getLogger(RedisDictProperties.class);

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	public String getdictValueByCode(String dictCode) {
		String value = "";
		try {
			value = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, dictCode);
			if (StringUtils.isNotEmpty(value)) {
				value = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, dictCode);
			}
//			logger.info("getdictValueByCode--->key[{}] value[{}]", dictCode, value);
		} catch (Exception ex) {
			logger.error("## getdictValueByCode is error" + ex);
		}
		return value;
	}

	/**
	 * 获取渠道签名KEY
	 * 
	 * @param channelCode
	 * @return
	 */
	public String getChannelKeyByCode(String channelCode) {
		String value = "";
		try {
			value = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_CHANNEL_SECURITY_INF_KV,
					channelCode);
			if (StringUtils.isNotEmpty(value)) {
				value = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_CHANNEL_SECURITY_INF_KV,
						channelCode);
			}
		} catch (Exception ex) {
			logger.error("## getChannelKeyByCode is error" + ex);
		}
		return value;
	}
}
