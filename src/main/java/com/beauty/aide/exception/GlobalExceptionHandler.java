package com.beauty.aide.exception;

import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.result.ResultDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author shujie
 */
@Slf4j(topic = "handler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultDO<Void> excHandler(Exception e) {
        log.error("error:",e);
        return ResultDO.errorOf(CommonErrorCode.SYS_ERROR);
    }

}
