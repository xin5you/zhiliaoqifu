package com.ebeijia.zl.api.bm001.api.req;

import lombok.Data;

@Data
public class PayBillReq implements  java.io.Serializable{

	private static final long serialVersionUID = -11666576986195266L;

	private String mobileNo;

	private String rechargeAmount;

	private String outerTid;

	private String callback;

	private String itemId;
}
