package com.ebeijia.zl.web.api.model.telephone.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;

public interface ApiRechargeMobileService {


	BaseResult payment(TeleReqVO reqVo) throws Exception;
}
