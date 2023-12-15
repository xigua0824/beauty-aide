package com.beauty.aide.common.enums;

/**
 * @author xiaoliu
 */
public enum AccountStatusEnum {
    ACTIVE("已激活"),
    DISABLED("已禁用"),
    ;

    private String desc;

    AccountStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String getByName(String name) {
        for (AccountStatusEnum e : AccountStatusEnum.values()) {
            if (e.name().equals(name)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
