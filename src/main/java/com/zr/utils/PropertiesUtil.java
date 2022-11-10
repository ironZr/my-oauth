package com.zr.utils;

import cn.hutool.core.convert.Convert;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropertiesUtil implements EmbeddedValueResolverAware {

    private static StringValueResolver resolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        PropertiesUtil.resolver = resolver;
    }


    /**
     * 获取配置文件内容
     *
     * @param key 配置文件的key {@link com.zr.constant.PropertiesConstant}
     * @return
     */
    public static <T> T getPropertiesValue(String key, Class<T> clazz) {
        //拼接key
        String name = "${" + key + "}";
        return Convert.convert(clazz, resolver.resolveStringValue(name));
    }

    /**
     * 获取配置文件内容
     *
     * @param key 配置文件的key {@link com.zr.constant.PropertiesConstant}
     * @return
     */
    public static String getPropertiesValue(String key) {
        //拼接key
        String name = "${" + key + "}";
        return resolver.resolveStringValue(name);
    }

}