package com.snfq.common.base;

/**
 * 基础信息
 * @author fujin
 * @version $Id: CustomerSaveDelegate.java, v 0.1 2017-10-11 10:11 Exp $$
 */
public class BaseEntity {

    private Integer id;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页显示数量
     */
    private Integer rows = 10;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
