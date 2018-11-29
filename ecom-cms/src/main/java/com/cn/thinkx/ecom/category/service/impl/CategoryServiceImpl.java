package com.cn.thinkx.ecom.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.basics.goods.service.GoodsCategoryService;
import com.cn.thinkx.ecom.category.service.CategoryService;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsCategoryListShowType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.you163.api.vo.goods.APICategoryTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	@Qualifier("goodsCategoryService")
	private GoodsCategoryService goodsCategoryService;

	@Override
	public PageInfo<GoodsCategory> getCategoryPage(int startNum, int pageSize, GoodsCategory category) {
		PageHelper.startPage(startNum, pageSize);
		List<GoodsCategory> list = goodsCategoryService.getGoodsCategory(category);
		PageInfo<GoodsCategory> page = new PageInfo<GoodsCategory>(list);
		page.getList().stream().filter(c -> {
			c.setEcomType(GoodsEcomCodeType.findByCode(c.getEcomCode()).getValue());
			c.setListShowType(GoodsCategoryListShowType.findByCode(c.getListShow()).getValue());
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public PageInfo<GoodsCategory> getSecondCateGoryPage(int startNum, int pageSize, GoodsCategory category) {
		PageHelper.startPage(startNum, pageSize);
		List<GoodsCategory> list = goodsCategoryService.getSecondGoodsCateGory(category);
		PageInfo<GoodsCategory> page = new PageInfo<GoodsCategory>(list);
		page.getList().stream().filter(c -> {
			c.setEcomType(GoodsEcomCodeType.findByCode(c.getEcomCode()).getValue());
			c.setListShowType(GoodsCategoryListShowType.findByCode(c.getListShow()).getValue());
			return true;
		}).collect(Collectors.toList());
		return page;
	}
	

	@Override
	public PageInfo<GoodsCategory> getGoodsCategoryListPage(int startNum, int pageSize, GoodsCategory category) {
		PageHelper.startPage(startNum, pageSize);
		List<GoodsCategory> list = goodsCategoryService.getGoodsCategoryList(category);
		PageInfo<GoodsCategory> page = new PageInfo<GoodsCategory>(list);
		page.getList().stream().filter(c -> {
			c.setEcomType(GoodsEcomCodeType.findByCode(c.getEcomCode()).getValue());
			c.setListShowType(GoodsCategoryListShowType.findByCode(c.getListShow()).getValue());
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public GoodsCategory getGoodsCategoryByPrimaryKey(String primaryKey) {
		try {
			return goodsCategoryService.selectByPrimaryKey(primaryKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateGoodsCategory(GoodsCategory category) {
		try {
			return goodsCategoryService.updateByPrimaryKey(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void saveGoodsCategory(List<APICategoryTO> list, String ecomCode) throws Exception {
		saveCategory("0", list, 1, ecomCode);
	}
	
	private void saveCategory(String id, List<APICategoryTO> list, int leavl, String ecomCode) {
		GoodsCategory category = new GoodsCategory();
		for (APICategoryTO cateVo : list) {
			int tmp = leavl;
			category.setCatName(cateVo.getName());
			category.setEcomCode(ecomCode);
			category.setCatPath(String.valueOf(tmp));
			category.setParentId(id);
			category.setOuterCatId(cateVo.getId());
			try {
				category = goodsCategoryService.saveGoodsCategory(category);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cateVo.getSubCateList() != null && cateVo.getSubCateList().size() > 0) {
				tmp++;
				saveCategory(category.getCatId(), cateVo.getSubCateList(), tmp, ecomCode);
			}
		}
	}

}
