package com.beauty.aide.common.model.so;

import lombok.Data;

import java.io.Serializable;


@Data
public class BaseSO implements Serializable {

    private static final long serialVersionUID = -5053973674804642947L;

    protected Integer currentPage;
    protected Integer pageSize;

    protected int skip;
    protected int limit;

    public void calSkip() {
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        skip = (currentPage - 1) * pageSize;

        limit = pageSize;
    }
}
