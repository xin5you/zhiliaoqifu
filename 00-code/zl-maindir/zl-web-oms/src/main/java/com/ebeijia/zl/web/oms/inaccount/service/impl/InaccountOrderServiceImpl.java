package com.ebeijia.zl.web.oms.inaccount.service.impl;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.web.oms.inaccount.mapper.InaccountOrderMapper;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        if (orderList != null && orderList.size() >= 1) {
            for (InaccountOrder o : orderList) {
                if (!StringUtil.isNullOrEmpty(o.getRemitAmt())) {
                    o.setRemitAmt(new BigDecimal(NumberUtils.RMBCentToYuan(o.getRemitAmt().toString())));
                }
                o.setInaccountAmt(new BigDecimal(NumberUtils.RMBCentToYuan(o.getInaccountAmt().toString())));
            }
        }
        PageInfo<InaccountOrder> page = new PageInfo<InaccountOrder>(orderList);
        return page;
    }

    @Override
    public InaccountOrder getInaccountOrderByOrderId(String orderId) {
        return inaccountOrderMapper.getInaccountOrderByOrderId(orderId);
    }

    @Override
    public InaccountOrder getInaccountOrderByOrderIdAndCompanyId(InaccountOrder inaccountOrder) {
        return inaccountOrderMapper.getInaccountOrderByOrderIdAndCompanyId(inaccountOrder);
    }

}
