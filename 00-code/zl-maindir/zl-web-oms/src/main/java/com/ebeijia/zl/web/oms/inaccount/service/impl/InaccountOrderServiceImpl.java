package com.ebeijia.zl.web.oms.inaccount.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.inaccount.mapper.InaccountOrderMapper;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;

import java.util.List;


/**
 *
 * tb_inaccount_order Service 实现类
 *
 * @User myGen
 * @Date 2018-12-19
 */
@Service
public class InaccountOrderServiceImpl extends ServiceImpl<InaccountOrderMapper, InaccountOrder> implements InaccountOrderService{

    @Autowired
    private InaccountOrderMapper inaccountOrderMapper;

    @Override
    public List<InaccountOrder> getInaccountOrderByOrder(InaccountOrder inaccountOrder) {
        return inaccountOrderMapper.getInaccountOrderByOrder(inaccountOrder);
    }

    @Override
    public PageInfo<InaccountOrder> getInaccountOrderByOrderPage(int startNum, int pageSize, InaccountOrder inaccountOrder) {
        PageHelper.startPage(startNum, pageSize);
        List<InaccountOrder> orderList = inaccountOrderMapper.getInaccountOrderByOrder(inaccountOrder);
        PageInfo<InaccountOrder> page = new PageInfo<InaccountOrder>(orderList);
        return page;
    }

    @Override
    public InaccountOrder getInaccountOrderByOrderId(String orderId) {
        return inaccountOrderMapper.getInaccountOrderByOrderId(orderId);
    }
}
