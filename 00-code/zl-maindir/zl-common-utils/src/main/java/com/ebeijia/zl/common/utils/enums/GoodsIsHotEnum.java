package com.ebeijia.zl.common.utils.enums;

/**
 * 是否是热销商品
 * @author Administrator
 *
 */
public enum GoodsIsHotEnum {

    GoodsIsOpenEnum_0("0", "不是"),
    GoodsIsOpenEnum_1("1", "是");

    private String code;
    private String name;

    GoodsIsHotEnum(String code, String name){
        this.code=code;
        this.name=name;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public static GoodsIsHotEnum findByBId(String code) {
        for (GoodsIsHotEnum t : GoodsIsHotEnum.values()) {
            if (t.code.equalsIgnoreCase(code)) {
                return t;
            }
        }
        return null;
    }
}