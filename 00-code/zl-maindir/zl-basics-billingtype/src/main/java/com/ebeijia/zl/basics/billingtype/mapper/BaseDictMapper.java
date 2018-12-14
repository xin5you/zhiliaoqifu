package com.ebeijia.zl.basics.billingtype.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.billingtype.domain.BaseDict;

/**
 * 字典信息
 * @author Administrator
 *
 */
@Mapper
public interface BaseDictMapper extends BaseMapper<BaseDict> {
	
	List<BaseDict> getBaseDictList(BaseDict baseDict);

}
