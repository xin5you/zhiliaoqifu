package com.ebeijia.zl.service.telrecharge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.TelrechargeApp;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelrechargeApp.class)
public class AppTest {

	@Autowired
	private CompanyInfFacade companyInfFacade;
	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;
	@Autowired
	private RetailChnlProductInfFacade retailChnlProductInfFacade;
	@Autowired
	private RetailChnlItemListFacade retailChnlItemListFacade;
	@Autowired
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;
	@Autowired
	private ProviderInfFacade providerInfFacade;
	@Autowired
	private ProviderOrderInfFacade providerOrderInfFacade;
	
	
	@Test
	public void chnl() throws Exception{
		CompanyInf company = new CompanyInf();
		company.setCompanyId("142412");
		company.setName("111");
		company.setDataStat("0");
		company.setCreateTime(System.currentTimeMillis());
		company.setUpdateTime(System.currentTimeMillis());
		company.setLockVersion(0);
		companyInfFacade.deleteCompanyInf(company);
		
		/*RetailChnlInf a = new RetailChnlInf();
		a.setChannelId("1");
		a.setChannelCode("1");
		a.setChannelKey("1");
		a.setChannelName("1");
		a.setChannelPrewarningAmt(new BigDecimal(2));
		a.setChannelReserveAmt(new BigDecimal(4));
		a.setCreateTime(System.currentTimeMillis());
		a.setCreateUser("1");
		a.setDataStat("0");
		a.setEmail("1");
		a.setIsOpen("0");
		a.setLockVersion(0);
		a.setPhoneNo("1");
		a.setUpdateTime(System.currentTimeMillis());
		a.setUpdateUser("1");
		retailChnlInfFacade.getRetailChnlInfById("1");*/
	}
}
