package com.beauty.aide.common.enums;

public enum UserRoleEnum {
    SUPER_ADMIN(1L, "超级管理员"),
    ADMIN(2L, "管理员"),
    COMMON_USER(3L, "普通用户"),
    ;

    private String desc;
    private Long code;

    UserRoleEnum(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(Long code) {
        for (UserRoleEnum e : UserRoleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public Long getCode() {
        return code;
    }
}
