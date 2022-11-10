package com.zr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//允许的Origin头
        corsConfiguration.addAllowedHeader("*");//浏览器CORS请求会额外发送的头信息字段
        corsConfiguration.addAllowedMethod("*");//浏览器的CORS请求会用到哪些HTTP方法 GET PUT ...
//        corsConfiguration.setAllowCredentials(true);//true 服务器要浏览器发送Cookie 开启这个  addAllowedOrigin 不能为 *
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
