package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomPayCard;
import com.ebeijia.zl.shop.vo.CardBindInfo;
import com.ebeijia.zl.shop.vo.CardInfo;

public interface ICardService {
    CardInfo validCardNum(String cardNum);

    TbEcomPayCard listAccountCard();

    Integer bindCard(CardBindInfo card);

}
