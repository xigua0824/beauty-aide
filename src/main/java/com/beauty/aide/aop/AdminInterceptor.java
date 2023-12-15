package com.beauty.aide.aop;

import com.beauty.aide.constant.UserConstant;
import com.beauty.aide.common.model.vo.AccountVO;
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
            "/api/account/register",
            "/api/account/login"
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
            throw new IllegalStateException("111");
        }else {
            // @TODO 已登录，查看是否拥有接口权限
        }
        return true;
    }
}
