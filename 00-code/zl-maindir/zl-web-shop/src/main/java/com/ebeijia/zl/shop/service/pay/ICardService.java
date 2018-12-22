package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.vo.CardInfo;

public interface ICardService {
    CardInfo validCardNum(String cardNum);

}
