package com.beauty.aide.common.errors;

public enum UserErrorCode implements IErrorCode {

    USER_NOT_EXIST("用户不存在"),
    USER_NOT_LOGIN("用户没有登录"),
    SESSION_ERROR("session错误"),

    USER_LOGIN_FAILED("登录失败"),

    ACCOUNT_EXISTS("账号已存在"),

    PASSWORD_ERROR("密码错误"),

    PASSWORD_ERROR_TOO_MANY("密码错误次数过多，请2小时后再试"),

    PHONE_NUM_USED("手机号已被占用"),
    ;
    /**
     * 错误信息
     */
    private String message;

    private UserErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
