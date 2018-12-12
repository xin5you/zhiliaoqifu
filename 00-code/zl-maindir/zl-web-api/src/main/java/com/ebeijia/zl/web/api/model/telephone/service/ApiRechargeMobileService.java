package com.ebeijia.zl.web.api.model.telephone.service;

import com.ebeijia.zl.facade.telrecharge.resp.TeleReqVO;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;

public interface ApiRechargeMobileService {
	
	@SuppressWarnings("rawtypes")
	TeleRespDomain payment(TeleReqVO reqVo) throws Exception;
}
