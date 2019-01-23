package com.ebeijia.zl.web.oms.common.util;

import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.common.service.RecursiveAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecursiveUtil<T> {
    private List<T> list;
    private List<T> result;
    private RecursiveAdapter<T> adapterRules;

    public RecursiveUtil(RecursiveAdapter<T> adapterRules) {
        this.adapterRules = adapterRules;
    }

    public List<T> recursiveHandle(List<T> list, String pid) {
        this.list = list;
        result = new ArrayList<>();
        return recursive(pid);
    }

    private List<T> recursive(String pid) {
        if (adapterRules == null || list == null)
            return null;
        for (T t : list) {
            if (strHandle(adapterRules.getPid(t)).equals(strHandle(pid)) || strHandle(adapterRules.getPid(t)) == strHandle(pid)) {
                result.add(t);
                recursive(adapterRules.getId(t));
            }
        }
        return result;
    }

    private String strHandle(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return "";
        }
        return str;
    }
}
