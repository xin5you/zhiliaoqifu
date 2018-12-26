package com.ebeijia.zl.shop.vo;

import lombok.Data;

@Data
public class CardInfo {

    private String cardType;

    private String bank;

    private String bankName;

    private String key;

    private Object[] messages;

    private boolean validated;

    private String ok;


    public CardInfo(){}
}
