package com.ebeijia.zl.web.oms.providerChnl.service;

import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ProviderInfService {

	int providerOpenAccount(HttpServletRequest req);

	Map<String, Object> addProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	int addProviderTransferCommit(HttpServletRequest req);

	Map<String, Object> updateProviderRemitStatCommit(HttpServletRequest req);

	Map<String, Object> editProviderTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile);

	Map<String, Object> deleteProviderTransfer(HttpServletRequest req);
}
