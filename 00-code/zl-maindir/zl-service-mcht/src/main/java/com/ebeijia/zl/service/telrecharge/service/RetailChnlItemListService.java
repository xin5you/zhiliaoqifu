package com.ebeijia.zl.service.telrecharge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;


/**
 *
 * 分销商产品关联供应商商品表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface RetailChnlItemListService extends IService<RetailChnlItemList> {

	int deleteByProductId(String productId);
}
