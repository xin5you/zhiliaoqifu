package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LogDetail implements Serializable {

    private static final long serialVersionUID = -1L;

    private PageInfo<AccountLogVO> deals;

    private Map<String,TbEcomItxLogDetail> list;

}
