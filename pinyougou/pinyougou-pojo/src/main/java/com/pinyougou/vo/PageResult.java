package com.pinyougou.vo;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
    //总记录数
    private long total;
    //?表示类型不定而且使用了?后该对象的值初始化后里面的内容是不可以改变的
    //记录列表
    private List<?> rows;

    public PageResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
