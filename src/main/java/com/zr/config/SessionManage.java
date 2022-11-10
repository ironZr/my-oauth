package com.zr.config;

import com.zr.pojo.vo.SsoUserVO;
import org.springframework.stereotype.Component;

@Component
public class SessionManage {

    private static ThreadLocal<SsoUserVO> userSession = new ThreadLocal<>();

    public static void set(SsoUserVO ssoUserVO) {
        userSession.set(ssoUserVO);
    }

    public static SsoUserVO get() {
        return userSession.get();
    }

    public static void clear() {
        userSession.remove();
    }

}
