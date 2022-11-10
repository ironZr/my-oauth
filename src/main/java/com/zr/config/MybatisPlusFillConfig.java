package com.zr.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class MybatisPlusFillConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        this.strictInsertFill(metaObject, "createTime", Date.class, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        this.strictInsertFill(metaObject, "updateTime", Date.class, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ...");
        this.strictInsertFill(metaObject, "updateTime", Date.class, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }
}