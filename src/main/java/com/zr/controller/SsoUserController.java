package com.zr.controller;

import com.zr.common.PageResult;
import com.zr.common.R;
import com.zr.pojo.dto.SsoUserAddDTO;
import com.zr.pojo.dto.SsoUserPageDTO;
import com.zr.pojo.vo.SsoPageVO;
import com.zr.service.SsoUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "用户")
@Validated
public class SsoUserController {

    @Resource
    private SsoUserService ssoUserService;

    @GetMapping("/page")
    @ApiOperation("分页获取")
    public R<PageResult<SsoPageVO>> pageList(@Valid SsoUserPageDTO ssoUserPageDTO) {
        return R.ok(ssoUserService.pageList(ssoUserPageDTO));
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public R add(@RequestBody @Valid SsoUserAddDTO ssoUserAddDTO) {
        ssoUserService.add(ssoUserAddDTO);
        return R.ok();
    }

    @PostMapping("/disabled/{id}")
    @ApiOperation("禁用")
    public R disabled(@PathVariable(value = "id") Long id) {
        ssoUserService.disabled(id);
        return R.ok();
    }


}
