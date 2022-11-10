package com.zr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zr.common.PageResult;
import com.zr.config.SessionManage;
import com.zr.constant.SsoConstant;
import com.zr.dao.SsoUserMapper;
import com.zr.pojo.dto.SsoUserAddDTO;
import com.zr.pojo.dto.SsoUserPageDTO;
import com.zr.pojo.entity.SsoUser;
import com.zr.exception.PlatformException;
import com.zr.pojo.vo.SsoPageVO;
import com.zr.service.SsoUserService;
import com.zr.pojo.vo.SsoUserVO;
import com.zr.utils.CopyClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SsoUserServiceImpl extends ServiceImpl<SsoUserMapper, SsoUser> implements SsoUserService {

    @Override
    public SsoUserVO login(String account, String password) {
        SsoUser ssoUser = this.lambdaQuery().eq(SsoUser::getAccount, account).one();
        Optional.ofNullable(ssoUser).orElseThrow(() -> {
            log.info("用户不存在 account:[{}]", account);
            return new PlatformException("用户不存在");
        });

        if (!ssoUser.getIsDisabled().equals(SsoConstant.NO_DISABLED)) {
            throw new PlatformException("该用户以被禁用");
        }

        if (ssoUser.getPassword().equals(password)) {
            return SsoUserVO.builder().account(account).username(ssoUser.getUsername()).build();
        } else {
            log.info("用户密码错误 account:[{}] password:[{}]", account, password);
            throw new PlatformException("密码错误");
        }
    }

    @Override
    public PageResult<SsoPageVO> pageList(SsoUserPageDTO ssoUserPageDTO) {
        Page<SsoUser> page = this.lambdaQuery()
                .apply(StringUtils.isNotBlank(ssoUserPageDTO.getCondition()), "CONCAT_WS('',account,username) LIKE CONCAT('%',{0},'%')", ssoUserPageDTO.getCondition())
                .orderByDesc(SsoUser::getId)
                .page(new Page<>(ssoUserPageDTO.getPageNum(), ssoUserPageDTO.getRows()));
        List<SsoPageVO> ssoPageVOS = CopyClassUtil.copyWithCollection(page.getRecords(), SsoPageVO.class);
        return new PageResult<>(page.getTotal(), page.getPages(), ssoPageVOS);
    }

    @Override
    public void add(SsoUserAddDTO ssoUserAddDTO) {
        SsoUser ssoUser = CopyClassUtil.copyProperties(ssoUserAddDTO, SsoUser.class);
        ssoUser.setCreateUser(SessionManage.get().getUsername());
        try {
            this.save(ssoUser);
        } catch (DuplicateKeyException e) {
            throw new PlatformException("登录名重复!");
        }
    }

    @Override
    public void disabled(Long id) {
        this.lambdaUpdate().eq(SsoUser::getId, id).set(SsoUser::getIsDisabled, SsoConstant.IS_DISABLED).update();
    }


}
