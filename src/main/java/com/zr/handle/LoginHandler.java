package com.zr.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.zr.config.SessionManage;
import com.zr.constant.OauthConstant;
import com.zr.constant.PropertiesConstant;
import com.zr.constant.RedisKeyConstant;
import com.zr.exception.PlatformException;
import com.zr.pojo.vo.SsoUserVO;
import com.zr.utils.PropertiesUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LoginHandler implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader(OauthConstant.ACCESSTOKEN);
        if (ObjectUtil.isEmpty(accessToken)) throw new PlatformException(null, 401, "没登录,重定向");
        String key = RedisKeyConstant.ACESSTOKEN_KEY.concat(accessToken);
        Boolean flag = stringRedisTemplate.hasKey(key);
        if (Boolean.FALSE.equals(flag)) throw new PlatformException(null, 401, "登录已过期或不存在");

        //更新时间
        stringRedisTemplate.expire(key, PropertiesUtil.getPropertiesValue(PropertiesConstant.ACCESS_TIMEOUT, Long.class), TimeUnit.SECONDS);

        //管理当前会话
        String ssoUserJson = stringRedisTemplate.opsForValue().get(key);
        SsoUserVO ssoUserVO = JSONUtil.toBean(ssoUserJson, SsoUserVO.class);
        SessionManage.set(ssoUserVO);

        return Boolean.TRUE;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SessionManage.clear();
    }
}
