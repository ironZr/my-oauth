package com.zr.config;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Date;


@Configuration
public class WebDataConfig {

    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer = tar -> {
            tar.serializerByType(Long.class, ToStringSerializer.instance).serializerByType(Long.TYPE, ToStringSerializer.instance)
                    .serializerByType(Date.class, new JsonSerializer<Date>() {
                        @Override
                        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                            String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
                            jsonGenerator.writeString(format);
                        }
                    });
        };
        return customizer;
    }
}
