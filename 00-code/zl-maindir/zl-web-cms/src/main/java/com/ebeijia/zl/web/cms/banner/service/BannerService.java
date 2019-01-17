package com.ebeijia.zl.web.cms.banner.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomBanner;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface BannerService {

	/**
	 * 查询所有banner信息
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	 PageInfo<TbEcomBanner> getBannerListPage(int startNum, int pageSize, TbEcomBanner entity);

	/**
	 * 新增banner信息
	 * @param banner
	 * @param imageUrlFile
	 * @return
	 */
	 BaseResult<Object> addBanner(TbEcomBanner banner, MultipartFile imageUrlFile);

	/**
	 * 编辑banner信息
	 * @param banner
	 * @param imageUrlFile
	 * @return
	 */
	 BaseResult<Object> editBanner(TbEcomBanner banner, MultipartFile imageUrlFile);

	/**
	 * 删除banner信息
	 * @param banner
	 * @return
	 */
	BaseResult<Object> deleteBanner(HttpServletRequest req);
}
