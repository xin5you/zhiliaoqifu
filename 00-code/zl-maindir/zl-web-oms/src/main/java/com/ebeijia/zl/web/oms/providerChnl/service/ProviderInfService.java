package com.ebeijia.zl.web.oms.providerChnl.service;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ProviderInfService {

	int providerOpenAccount(HttpServletRequest req);
	
	int addProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	int addProviderTransferCommit(HttpServletRequest req);

	ModelMap updateProviderRemitStatCommit(HttpServletRequest req);

	int editProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);
}
