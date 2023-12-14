package com.beauty.aide.common.errors;


public interface IErrorCode {

    /**
     * 获取错误码
     * @return String
     */
    String getCode();

    /**
     * 获取错误信息
     * @return String
     */
    String getMessage();
}
