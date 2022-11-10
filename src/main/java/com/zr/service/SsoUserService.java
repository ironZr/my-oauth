package com.zr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zr.common.PageResult;
import com.zr.pojo.dto.SsoUserAddDTO;
import com.zr.pojo.dto.SsoUserPageDTO;
import com.zr.pojo.entity.SsoUser;
import com.zr.pojo.vo.SsoPageVO;
import com.zr.pojo.vo.SsoUserVO;


/**
 * @program: sy-sso
 * @description:
 * @author: zhangRui
 * @create: 2022-11-02 09:56
 **/
public interface SsoUserService extends IService<SsoUser> {
    /**
     * 登录
     *
     * @param account  account
     * @param password password
     * @return
     */
    SsoUserVO login(String account, String password);

    /**
     * 分页获取用户列表
     *
     * @param ssoUserPageDTO ssoUserPageDTO
     * @return
     */
    PageResult<SsoPageVO> pageList(SsoUserPageDTO ssoUserPageDTO);

    /**
     * 添加用户
     *
     * @param ssoUserAddDTO
     */
    void add(SsoUserAddDTO ssoUserAddDTO);

    /**
     * 禁用用户
     *
     * @param id id
     */
    void disabled(Long id);
}
