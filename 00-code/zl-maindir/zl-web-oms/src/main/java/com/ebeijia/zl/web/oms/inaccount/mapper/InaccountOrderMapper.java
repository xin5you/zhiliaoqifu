package com.ebeijia.zl.web.oms.inaccount.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;

import java.util.List;

/**
 *
 * tb_inaccount_order Mapper 接口
 *
 * @User myGen
 * @Date 2018-12-19
 */
@Mapper
public interface InaccountOrderMapper extends BaseMapper<InaccountOrder> {

    List<InaccountOrder> getInaccountOrderByOrder(InaccountOrder inaccountOrder);
}
