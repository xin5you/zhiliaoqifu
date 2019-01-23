package com.ebeijia.zl.shop.utils;


import com.ebeijia.zl.common.utils.tools.StringUtils;

import java.util.*;


public class CalculateUtil {

    /**
     * Map参数根据ASCII排序用&拼接
     *
     * @param params 需要排序拼接的Map
     */
    public static String sortSplice(Map<String, String> params) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            if (StringUtils.isNotBlank(item.getKey())) {
                String key = item.getKey();
                String val = item.getValue();
                buf.append(key + "=" + val);
                if (i < infoIds.size() - 1)
                    buf.append("&");
            }
        }
        return buf.toString();
    }

}

