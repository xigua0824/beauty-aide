package com.beauty.aide.constant;

import com.beauty.aide.common.enums.UserRoleEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户常量
 *
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

//    Map<String, List<String>> ROLE_PERMISSION = new HashMap<String, List<String>>(){{
//        put(UserRoleEnum.SUPER_ADMIN.name(), Arrays.asList("/**"));
//        put(UserRoleEnum.ADMIN.name(), Arrays.asList("/**"));
//        put(UserRoleEnum.COMMON_USER.name(), Arrays.asList(""));
//    }};

}
