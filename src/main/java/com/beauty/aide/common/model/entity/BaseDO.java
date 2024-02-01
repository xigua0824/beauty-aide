package com.beauty.aide.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xiaoliu
 */
@Data
public class BaseDO implements Serializable {
    private static final long serialVersionUID = -7381822006225981401L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 修改时间
     */
    protected LocalDateTime updateTime;

    /**
     * 状态
     */
    protected String status;


}
