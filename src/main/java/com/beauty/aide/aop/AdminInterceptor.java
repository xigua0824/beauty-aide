package com.beauty.aide.aop;

import com.beauty.aide.common.errors.ErrorCode;
import com.beauty.aide.common.vo.AccountVO;
import com.beauty.aide.constant.UserConstant;
import com.beauty.aide.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaoliu
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {


    private static final List<String> NO_LOGIN_PATHS = Arrays.asList(
            "/api/account/**"
    );

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        String uri = request.getRequestURI();

        // 未登录 抛异常
        if (accountVO == null) {
            for (String path : NO_LOGIN_PATHS) {
                if (PATH_MATCHER.match(path, uri)) {
                    return true;
                }
            }
//            return false;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");

//            throw new BusinessException(CommonErrorCode.NOT_PERMISSION.getMessage());
        }
        return true;
    }
}
