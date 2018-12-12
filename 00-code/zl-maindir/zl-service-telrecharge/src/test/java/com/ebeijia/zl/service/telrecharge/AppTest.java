package com.ebeijia.zl.service.telrecharge;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ebeijia.zl.TelrechargeApp;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
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
	private RetailChnlOrderInfFacade telChannelOrderInfFacade;
	@Autowired
	private ProviderInfFacade telProviderInfFacade;
	@Autowired
	private ProviderOrderInfFacade telProviderOrderInfFacade;
	
	
	@Test
	public void chnl() throws Exception{
		CompanyInf company = new CompanyInf();
		company.setAddress("aaa");
		company.setCompanyId("88b6fda4-0cc7-4be1-9495-0256a0bf30ac");
		companyInfFacade.getCompanyInfByLawCode("88b6fda4-0cc7-4be1-9495-0256a0bf30ac");
		
		RetailChnlInf a = new RetailChnlInf();
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
		/*retailChnlInfFacade.getRetailChnlInfById("1");*/
	}
}
