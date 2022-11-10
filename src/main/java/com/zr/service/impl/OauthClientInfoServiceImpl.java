package com.zr.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zr.config.SessionManage;
import com.zr.constant.OauthConstant;
import com.zr.dao.OauthClientInfoMapper;
import com.zr.pojo.dto.OauthAppInfoAddDTO;
import com.zr.pojo.dto.OauthAppInfoEditDTO;
import com.zr.pojo.entity.OauthAppInfo;
import com.zr.exception.PlatformException;
import com.zr.service.OauthAppInfoService;
import com.zr.utils.CopyClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class OauthClientInfoServiceImpl extends ServiceImpl<OauthClientInfoMapper, OauthAppInfo> implements OauthAppInfoService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(OauthAppInfoAddDTO oauthClientInfoAddDTO) {
        OauthAppInfo oauthAppInfo = this.lambdaQuery().eq(OauthAppInfo::getAppName, oauthClientInfoAddDTO.getAppName()).one();
        if (ObjectUtil.isNotEmpty(oauthAppInfo)) throw new PlatformException("客户端名称不能重复");
        String appId = retryAppid();
        String appSecret = RandomUtil.randomString(8);

        OauthAppInfo oauthClientInfo = CopyClassUtil.copyProperties(oauthClientInfoAddDTO, OauthAppInfo.class);
        oauthClientInfo.setAppId(appId);
        oauthClientInfo.setAppSecret(appSecret);
        oauthClientInfo.setCreateUser(SessionManage.get().getUsername());
        this.save(oauthClientInfo);
    }

    @Override
    public void edit(OauthAppInfoEditDTO oauthClientInfoEditDTO) {
        OauthAppInfo oauthClientInfo = CopyClassUtil.copyProperties(oauthClientInfoEditDTO, OauthAppInfo.class);
        oauthClientInfo.setUpdateUser(SessionManage.get().getUsername());
        this.updateById(oauthClientInfo);
    }

    @Override
    public void validate(String appId, String appSecret) {
        OauthAppInfo oauthClientInfo = this.lambdaQuery().eq(OauthAppInfo::getAppId, appId).one();

        Optional.ofNullable(oauthClientInfo).orElseThrow(() -> {
            log.info("appId[{}]不存在", appId);
            return new PlatformException("appId不存在!");
        });

        if (!appSecret.equals(oauthClientInfo.getAppSecret())) {
            log.info("appId[{}]  appSecret[{}] 密钥不正确", appId, appSecret);
            throw new PlatformException("密钥不正确!");
        }

        if (oauthClientInfo.getStatus().equals(OauthConstant.UN_AUTHORIZED))
            throw new PlatformException("暂未开通该客户端!");

        if (oauthClientInfo.getStatus().equals(OauthConstant.BAN_AUTHORIZED))
            throw new PlatformException("该客户端被禁用!");
    }

    @Override
    public void isExistClient(String appId) {
        OauthAppInfo oauthAppInfo = this.lambdaQuery().eq(OauthAppInfo::getAppId, appId).one();
        if (ObjectUtil.isEmpty(oauthAppInfo)) {
            log.info("appId[{}]不存在", appId);
            throw new PlatformException("appId不存在!");
        }
    }

    @Override
    public void editStatus(Long id) {
        this.lambdaUpdate()
                .eq(OauthAppInfo::getId, id)
                .set(OauthAppInfo::getStatus, OauthConstant.BAN_AUTHORIZED)
                .set(OauthAppInfo::getUpdateUser, SessionManage.get().getUsername())
                .update();
    }

    /**
     * 6次循环生成appId 防止重复
     * TODO 分布式锁来做
     */
    private String retryAppid() {
        int count = 5;
        String appId = null;
        for (int i = 0; i <= count; i++) {
            appId = RandomUtil.randomString(8);
            OauthAppInfo oauthAppInfo = this.lambdaQuery().eq(OauthAppInfo::getAppId, appId).one();
            if (ObjectUtil.isNull(oauthAppInfo)) {
                break;
            }
            if (i == count) {
                throw new PlatformException("请重试");
            }
        }
        return appId;
    }
}
