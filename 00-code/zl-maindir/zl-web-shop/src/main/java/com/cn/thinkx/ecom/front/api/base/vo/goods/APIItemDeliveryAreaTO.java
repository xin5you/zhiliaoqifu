package com.cn.thinkx.ecom.front.api.base.vo.goods;

import java.util.List;

public class APIItemDeliveryAreaTO {
	private String supportStatus;
	private List<APIItemDistrictSupportTO> supportedDistricts;
	public String getSupportStatus() {
		return supportStatus;
	}
	public void setSupportStatus(String supportStatus) {
		this.supportStatus = supportStatus;
	}
	public List<APIItemDistrictSupportTO> getSupportedDistricts() {
		return supportedDistricts;
	}
	public void setSupportedDistricts(List<APIItemDistrictSupportTO> supportedDistricts) {
		this.supportedDistricts = supportedDistricts;
	}
	
}
