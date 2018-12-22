package com.ebeijia.zl.web.oms.inaccount.service.impl;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.inaccount.mapper.InaccountOrderDetailMapper;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;

import java.math.BigDecimal;
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

    @Override
    public PageInfo<InaccountOrderDetail> getInaccountOrderDetailByOrderPage(int startNum, int pageSize, InaccountOrderDetail inaccountOrderDetail) {
        PageHelper.startPage(startNum, pageSize);
        List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailMapper.getInaccountOrderDetailByOrderId(inaccountOrderDetail.getOrderId());
        for (InaccountOrderDetail o : orderDetailList) {
            o.setTransAmt(new BigDecimal(NumberUtils.RMBCentToYuan(o.getTransAmt().toString())));
            o.setBNamae(SpecAccountTypeEnum.findByBId(o.getBId()).getName());
            o.setPlatformInAmt(new BigDecimal(NumberUtils.RMBCentToYuan(o.getPlatformInAmt().toString())));
            o.setCompanyInAmt(new BigDecimal(NumberUtils.RMBCentToYuan(o.getCompanyInAmt().toString())));
        }
        PageInfo<InaccountOrderDetail> page = new PageInfo<InaccountOrderDetail>(orderDetailList);
        return page;
    }

    @Override
    public InaccountOrderDetail getInaccountOrderDetailByOrderIdAndBid(InaccountOrderDetail inaccountOrderDetail) {
        return inaccountOrderDetailMapper.getInaccountOrderDetailByOrderIdAndBid(inaccountOrderDetail);
    }
}
