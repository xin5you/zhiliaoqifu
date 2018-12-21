package com.ebeijia.zl.api.bm001.api.req;

import lombok.Data;

@Data
public class HbkToBMOrderRequest implements  java.io.Serializable {

	private static final long serialVersionUID = -1259608733245370258L;

	private String userId;
	private String orderId;
	private String mobileNo;
	private String rechargeAmount;
	private String callback;
	private String channl;
	private String timestamp;
	private String sign;

	
}
