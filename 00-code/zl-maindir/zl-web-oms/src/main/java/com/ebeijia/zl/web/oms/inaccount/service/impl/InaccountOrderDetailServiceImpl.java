package com.ebeijia.zl.web.oms.inaccount.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.inaccount.mapper.InaccountOrderDetailMapper;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;

import java.util.List;


/**
 *
 * tb_inaccount_order_detail Service 实现类
 *
 * @User myGen
 * @Date 2018-12-19
 */
@Service
public class InaccountOrderDetailServiceImpl extends ServiceImpl<InaccountOrderDetailMapper, InaccountOrderDetail> implements InaccountOrderDetailService{

    @Autowired
    private InaccountOrderDetailMapper inaccountOrderDetailMapper;

    @Override
    public List<InaccountOrderDetail> getInaccountOrderDetailByOrderId(String orderId) {
        return inaccountOrderDetailMapper.getInaccountOrderDetailByOrderId(orderId);
    }
}
