package com.ebeijia.zl.service.telrecharge.facadeimpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlAreaInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlAreaInfFacade;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlAreaInfService;

@com.alibaba.dubbo.config.annotation.Service(version="1.0.0")
public class RetailChnlAreaInfFacadeImpl  implements RetailChnlAreaInfFacade {

	@Autowired
	private RetailChnlAreaInfService retailChnlAreaInfService;
	
	@Override
	public RetailChnlAreaInf getRetailChnlAreaInfById(String areaId) throws Exception {
		return retailChnlAreaInfService.getById(areaId);
	}

	@Override
	public boolean saveRetailChnlAreaInf(RetailChnlAreaInf retailChnlAreaInf) throws Exception {
		 return retailChnlAreaInfService.save(retailChnlAreaInf);
	}

	@Override
	public boolean updateRetailChnlAreaInf(RetailChnlAreaInf RetailChnlAreaInf) throws Exception {
		return retailChnlAreaInfService.updateById(RetailChnlAreaInf);
	}

	@Override
	public boolean deleteRetailChnlAreaInfById(String areaId) throws Exception {
		return retailChnlAreaInfService.removeById(areaId);
	}

	@Override
	public List<RetailChnlAreaInf> getRetailChnlAreaInfList(RetailChnlAreaInf retailChnlAreaInf) throws Exception {
		QueryWrapper<RetailChnlAreaInf> queryWrapper=new QueryWrapper<RetailChnlAreaInf>(retailChnlAreaInf);
		return retailChnlAreaInfService.list(queryWrapper);
	}
}
