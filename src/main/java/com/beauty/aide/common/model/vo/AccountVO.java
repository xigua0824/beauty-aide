package com.beauty.aide.common.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoliu
 */
@Data
public class AccountVO {

    private Long id;
    private String account;
    private String headImg;
    private String nickName;
    private Long roleId;
    private String roleName;
    private String statusDesc;

}
