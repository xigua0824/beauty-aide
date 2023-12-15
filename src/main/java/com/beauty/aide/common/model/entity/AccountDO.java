package com.beauty.aide.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoliu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDO extends BaseDO {

    private Long roleId;
    private String account;
    private String password;
    private String headImg;
    private String nickName;

}
