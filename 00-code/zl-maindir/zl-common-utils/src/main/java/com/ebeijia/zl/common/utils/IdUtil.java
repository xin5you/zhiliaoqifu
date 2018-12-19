package com.ebeijia.zl.common.utils;

import java.util.UUID;
/**
 * 
* 
* @ClassName: IdUtil.java
* @Description: ID工具包
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月3日 上午11:09:51 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月3日     zhuqi           v1.0.0
 */
public class IdUtil {

	/**
	 * 
	* @Function: IdUtil.java
	* @Description: 
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 上午11:11:14 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public static String getNextId(){
		return UUID.randomUUID().toString();
	}
	
}
