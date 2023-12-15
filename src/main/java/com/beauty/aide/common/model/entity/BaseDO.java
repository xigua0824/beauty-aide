package com.beauty.aide.common.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaoliu
 */
@Data
public class BaseDO implements Serializable {
    private static final long serialVersionUID = -7381822006225981401L;

    /**
     * 主键
     */
    protected Long id;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改时间
     */
    protected Date updateTime;

    /**
     * 状态
     */
    protected String status;


}
