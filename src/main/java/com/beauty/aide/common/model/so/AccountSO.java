package com.beauty.aide.common.model.so;

import lombok.Data;

/**
 * @author xiaoliu
 */
@Data
public class AccountSO extends BaseSO {
    private static final long serialVersionUID = -4243742749263159391L;
    private String nickName;
    private String account;
    private Long roleId;
}
