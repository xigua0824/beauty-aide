package com.beauty.aide.common.result;

import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.errors.IErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class ResultDO<T> implements Serializable {

    private static final long serialVersionUID = 5295229877049937898L;

    protected String stackTrace;

    protected Boolean success = false;

    protected String errorCode;

    protected String errorMsg;

    private T content;

    public ResultDO() {
    }

    public ResultDO(boolean success, String code, String message, T data) {
        this.errorCode = code;
        this.errorMsg = message;
        this.content = data;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResultDO<T> asFail() {
        return ResultDO.errorOf(errorCode, errorMsg);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("code", errorCode);
        map.put("msg", errorMsg);
        map.put("data", content);
        return map;
    }

    /**
     * 推荐使用：因为EnumCode可以扩展
     */
    public static <T> ResultDO<T> errorOf(IErrorCode iErrorCode) {
        return errorOf(iErrorCode.getCode(), iErrorCode.getMessage());
    }

    public static <T> ResultDO<T> errorOf(String code, String message) {
        ResultDO<T> cr = new ResultDO<>();
        cr.setSuccess(false);
        cr.setErrorCode(code);
        cr.setErrorMsg(message);
        return cr;
    }

    public static <T> ResultDO<T> errorOf(String message) {
        ResultDO<T> cr = new ResultDO<>();
        cr.setSuccess(false);
        cr.setErrorCode(CommonErrorCode.SYS_ERROR.getCode());
        cr.setErrorMsg(message);
        return cr;
    }

    public static <T> ResultDO<T> succOf(T data) {
        return new ResultDO<T>(true, "SUCCESS", "success", data);
    }

    public static <T> ResultDO<T> succOf(T data, String message) {
        return new ResultDO<T>(true, null, message, data);
    }

    public static <T> ResultDO<T> succOf() {
        return succOf(null);
    }

    public static <T> ResultDO<T> init() {
        return init(false);
    }

    public static <T> ResultDO<T> init(boolean isSuccess) {
        ResultDO<T> cr = new ResultDO<T>();
        cr.setSuccess(isSuccess);
        return cr;
    }
}
