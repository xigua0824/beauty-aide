package com.beauty.aide.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaoliu
 */
public class CommonUtil {

    public static String md5(String content) {
        return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String md5(String content, String salt) {
        return md5(String.format("%s%s", content, salt));
    }

}
