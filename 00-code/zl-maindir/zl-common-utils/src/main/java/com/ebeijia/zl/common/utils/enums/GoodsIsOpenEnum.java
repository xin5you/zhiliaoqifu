package com.ebeijia.zl.common.utils.enums;

/**
 * 商品是否开启状态
 * @author Administrator
 *
 */
public enum GoodsIsOpenEnum {

    GoodsIsOpenEnum_0("0", "未开启"),
    GoodsIsOpenEnum_1("1", "已开启");

    private String code;
    private String name;

    GoodsIsOpenEnum(String code, String name){
        this.code=code;
        this.name=name;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public static GoodsIsOpenEnum findByBId(String code) {
        for (GoodsIsOpenEnum t : GoodsIsOpenEnum.values()) {
            if (t.code.equalsIgnoreCase(code)) {
                return t;
            }
        }
        return null;
    }
}