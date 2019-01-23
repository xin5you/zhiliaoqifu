package com.ebeijia.zl.web.oms.common.service;

public interface RecursiveAdapter<T> {
    /**
     * 获得id
     * @param t
     * @return
     */
    public String getId(T t);

    /**
     * 获得pid
     * @param t
     * @return
     */
    public String getPid(T t);
}
