package com.ebeijia.zl.shop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.dao.goods.service.*;
import com.ebeijia.zl.shop.service.category.ICategoryService;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.ebeijia.zl.shop.vo.GoodsDetailInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ITbEcomGoodsService goodsDao;

    @Autowired
    private ITbEcomGoodsDetailService detailDao;

    @Autowired
    private ITbEcomGoodsGalleryService galleryDao;

    @Autowired
    private ITbEcomGoodsProductService productDao;

    @Autowired
    private ITbEcomSpecificationService specificationDao;

    @Autowired
    private ITbEcomSpecValuesService specValuesDao;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITbEcomGoodsSpecService goodsSpecDao;

    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public PageInfo<Goods> listGoods(String billingType, String catid, String orderby, Integer start, Integer limit) {
        if (limit == null || limit > 100) {
            limit = Integer.valueOf(20);
        }
        if (start == null) {
            start = Integer.valueOf(0);
        }

        //TODO orderBy
        List<Goods> goodsList = null;
        PageHelper.startPage(start, limit);
        SpecAccountTypeEnum type = SpecAccountTypeEnum.findByBId(billingType);
        Goods goods = new Goods();
        if (type != null) {
            //TODO 优化sql
            String bId = type.getbId();
            goods.setBId(bId);
        }

        if (catid != null) {
            goods.setCatId(catid);
            goodsList = goodsDao.getGoodsByCategory(goods);
        } else {
            goodsList = goodsDao.getGoodsList(goods);
        }
        PageInfo<Goods> page = new PageInfo<>(goodsList);
//        page.getList().stream().filter(d ->{
//            if(!StringUtil.isNullOrEmpty(d.getGoodsDetail())){
//                goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
//            }
//            return true;
//        }).collect(Collectors.toList());
        return page;
    }

    @Override
    public PageInfo<Goods> listGoods(String billingType, String orderby, Integer start, Integer limit) {
        return listGoods(billingType, null, orderby, start, limit);
    }

    @Override
    public GoodsDetailInfo getDetail(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsDetail detail = new TbEcomGoodsDetail();
        detail.setGoodsId(goodsId);
        GoodsDetailInfo goodsDetailInfo = new GoodsDetailInfo();
        goodsDetailInfo.setDetail(detailDao.getOne(new QueryWrapper<>(detail)));
        goodsDetailInfo.setInfo(goodsDao.getById(goodsId));

        //构建查询器
        TbEcomGoodsProduct query = new TbEcomGoodsProduct();
        query.setGoodsId(goodsId);

        //获取最大最小金额
        List<TbEcomGoodsProduct> products = productDao.list(new QueryWrapper<>(query));
        products.sort(Comparator.comparing(TbEcomGoodsProduct::getGoodsPrice));
        goodsDetailInfo.setMinPrice(Long.valueOf(products.get(0).getGoodsPrice()));
        goodsDetailInfo.setMaxPrice(Long.valueOf(products.get(products.size() - 1).getGoodsPrice()));
        makeProductMap(goodsDetailInfo, products);
        //TODO

        TbEcomGoodsSpec tbEcomGoodsSpec = new TbEcomGoodsSpec();
        tbEcomGoodsSpec.setGoodsId(goodsId);
        List<TbEcomGoodsSpec> goodsSpecs = goodsSpecDao.list(new QueryWrapper<>(tbEcomGoodsSpec));
        makeSpecAndValueNames(goodsDetailInfo, goodsSpecs);
        makeSpecsMap(goodsDetailInfo, goodsSpecs);
        return goodsDetailInfo;
    }

    private void makeSpecAndValueNames(GoodsDetailInfo goodsDetailInfo, List<TbEcomGoodsSpec> goodsSpecs) {
        List<String> specIds = new LinkedList<>();
        List<String> valueIds = new LinkedList<>();

        Iterator<TbEcomGoodsSpec> iterator = goodsSpecs.iterator();
        while(iterator.hasNext()){
            TbEcomGoodsSpec next = iterator.next();
            specIds.add(next.getSpecId());
            valueIds.add(next.getSpecValueId());
        }
        //list转Map
        Map<String, TbEcomSpecification> specificationMap = specificationDao.listByIds(specIds).stream().collect(Collectors.toMap(TbEcomSpecification::getSpecId, a -> a, (k1, k2) -> k1));
        Map<String, TbEcomSpecValues> specValuesMap = specValuesDao.listByIds(valueIds).stream().collect(Collectors.toMap(TbEcomSpecValues::getSpecValueId, a -> a, (k1, k2) -> k1));
        goodsDetailInfo.setSpecNames(specificationMap);
        goodsDetailInfo.setValueNames(specValuesMap);
    }

    private Map<String, Map<String, List<String>>> makeSpecsMap(GoodsDetailInfo goodsDetailInfo, List<TbEcomGoodsSpec> goodsSpecs) {
        Map<String, Map<String, List<String>>> result = new HashMap<>();
        Map<TbEcomSpecification, List<String>> map = new HashMap<>();
        Iterator<TbEcomGoodsSpec> iterator = goodsSpecs.iterator();
        while (iterator.hasNext()) {
            TbEcomGoodsSpec spec = iterator.next();
            if (result.get(spec.getSpecId()) == null) {
                Map<String, List<String>> init = new HashMap<>();
                result.put(spec.getSpecId(), init);
            }
            Map<String, List<String>> specValuesAndSKU = result.get(spec.getSpecId());
            if (specValuesAndSKU.get(spec.getSpecValueId()) == null) {
                specValuesAndSKU.put(spec.getSpecValueId(), new LinkedList<>());
            }
            List<String> values = specValuesAndSKU.get(spec.getSpecValueId());
            values.add(spec.getProductId());
        }
        goodsDetailInfo.setSpecsMap(result);
        return result;
    }

    private HashMap<String, TbEcomGoodsProduct> makeProductMap(GoodsDetailInfo goodsDetailInfo, List<TbEcomGoodsProduct> products) {
        HashMap<String, TbEcomGoodsProduct> map = new HashMap<>();
        Iterator<TbEcomGoodsProduct> iterator = products.iterator();
        while (iterator.hasNext()) {
            TbEcomGoodsProduct next = iterator.next();
            if (next == null || next.getProductId() == null) {
                continue;
            }
            map.put(next.getProductId(), next);
        }
        goodsDetailInfo.setProducts(map);
        return map;
    }

    @Override
    public List<TbEcomGoodsGallery> getGallery(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsGallery gallery = new TbEcomGoodsGallery();
        gallery.setGoodsId(goodsId);
        return galleryDao.list(new QueryWrapper<>(gallery));
    }

    @Override
    public List<TbEcomGoodsProduct> listSkuByGoodsId(String goodsId) {
        if (!vaildId(goodsId)) {
            return null;
        }
        TbEcomGoodsProduct product = new TbEcomGoodsProduct();
        product.setGoodsId(goodsId);
        return productDao.list(new QueryWrapper<>(product));
    }


    private boolean vaildId(String id) {
        if (StringUtils.isEmpty(id) || id.length() != 36) {
            return false;
        }
        return true;
    }

    @Override
    public void productStoreConsumer(String productId, int i) {
        if (i==0){
            return;
        }
        TbEcomGoodsProduct product = productDao.getById(productId);
        Integer isStore = product.getIsStore() - i ;
        if (isStore.compareTo(0)<0){
            throw new BizException(ResultState.NOT_ACCEPTABLE,"库存不足");
        }
        product.setIsStore(isStore);
        productDao.updateById(product);
    }

    @Override
    public void productStoreRecover(String productId, int i) {
        if (i==0){
            return;
        }
        TbEcomGoodsProduct product = productDao.getById(productId);
        Integer isStore = product.getIsStore()+i;
        product.setIsStore(isStore);
        productDao.updateById(product);

    }

    @Override
    public void productStoreChange(String productId, int i) {
        if (i==0){
            return;
        }
        TbEcomGoodsProduct product = productDao.getById(productId);
        Integer enableStore = product.getEnableStore()+i;
        if (enableStore.compareTo(0)<0){
            throw new BizException(ResultState.NOT_ACCEPTABLE,"库存调整异常");
        }
        product.setEnableStore(enableStore);
        productDao.updateById(product);
    }

}
