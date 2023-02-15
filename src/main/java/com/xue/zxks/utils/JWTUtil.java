package com.xue.zxks.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    public static String getTokenFrom(String authorization) {
        if (
            StringUtils.isNotBlank(authorization) &&
            authorization.toLowerCase().startsWith("bearer ")
        ) {
            return authorization.substring(7);
        }
        return null;
    }
}
