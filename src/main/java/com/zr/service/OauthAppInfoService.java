package com.zr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zr.pojo.dto.OauthAppInfoAddDTO;
import com.zr.pojo.dto.OauthAppInfoEditDTO;
import com.zr.pojo.entity.OauthAppInfo;


public interface OauthAppInfoService extends IService<OauthAppInfo> {

    /**
     * 添加接入客户端信息
     *
     * @param oauthAppInfoAddDTO
     */
    void add(OauthAppInfoAddDTO oauthAppInfoAddDTO);

    /**
     * 编辑接入客户端信息
     *
     * @param oauthAppInfoEditDTO
     */
    void edit(OauthAppInfoEditDTO oauthAppInfoEditDTO);

    /**
     * 校验应用
     *
     * @param appId     appId
     * @param appSecret appSecret
     */
    void validate(String appId, String appSecret);

    /**
     * 校验客户端
     *
     * @param appId appId
     */
    void isExistClient(String appId);

    /**
     * 编辑状态
     *
     * @param id id
     */
    void editStatus(Long id);
}
