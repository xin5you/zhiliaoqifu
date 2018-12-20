package com.ebeijia.zl.web.oms.providerChnl.service;

import javax.servlet.http.HttpServletRequest;

public interface ProviderInfService {

	int providerOpenAccount(HttpServletRequest req);
	
	int addProviderTransfer(HttpServletRequest req);
}
