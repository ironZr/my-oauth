package com.zr.config;

import com.google.common.collect.Lists;
import com.zr.constant.PropertiesConstant;
import com.zr.handle.LoginHandler;
import com.zr.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginHandler loginHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String exclsStr = PropertiesUtil.getPropertiesValue(PropertiesConstant.EXCLUDE_URLS);
        List<String> excludeUrLs = Lists.newArrayList(StringUtils.split(exclsStr, ","));
        registry.addInterceptor(loginHandler).addPathPatterns("/**").excludePathPatterns(excludeUrLs);
    }
}
