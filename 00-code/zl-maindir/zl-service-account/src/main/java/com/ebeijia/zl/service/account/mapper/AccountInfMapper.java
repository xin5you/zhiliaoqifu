package com.ebeijia.zl.service.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.account.dto.AccountInf;
import com.ebeijia.zl.facade.account.vo.AccountVO;

/**
 *
 * 用户账户信息 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Mapper
public interface AccountInfMapper extends BaseMapper<AccountInf> {
	
	
	/**
	 * 
	* @Description: 获取用户的账户列表
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:24:58 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	List<AccountVO> getAccountVOToUserList(String usreId);
	
	/**
	 * 
	* @Description: 获取企业的账户列表
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:25:22 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	List<AccountVO> getAccountVOToCompanyList(String companyId);
	
	/**
	 * 
	* @Description: 获取供应商的账户列表
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:25:46 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	List<AccountVO> getAccountVOToProviderList(String providerId);
	
	/**
	 * 
	* @Description: 获取分销商的账户列表
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:26:31 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	List<AccountVO> getAccountVOToRetailList(String channelId);

}
