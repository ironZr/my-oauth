package com.zr.controller;

import com.zr.common.R;
import com.zr.pojo.dto.OauthAppInfoAddDTO;
import com.zr.pojo.dto.OauthAppInfoEditDTO;
import com.zr.pojo.entity.OauthAppInfo;
import com.zr.service.OauthAppInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/oauth")
@Api(tags = "接入客户端")
public class Oauth2Controller {

    @Resource
    private OauthAppInfoService oauthClientInfoService;

    /**
     * app信息
     */
    @ApiOperation("客户端列表")
    @GetMapping("/list")
    public R list() {
        List<OauthAppInfo> oauthAppInfos = oauthClientInfoService.list();
        return R.ok(oauthAppInfos);
    }

    /**
     * 添加接入客户端信息
     */
    @ApiOperation("添加接入客户端信息")
    @PostMapping("/add")
    public R add(@Valid @RequestBody OauthAppInfoAddDTO oauthClientInfoAddDTO) {
        oauthClientInfoService.add(oauthClientInfoAddDTO);
        return R.ok();
    }

    /**
     * 编辑接入客户端信息
     */
    @ApiOperation("编辑接入客户端信息")
    @PostMapping("/edit")
    public R edit(@Valid @RequestBody OauthAppInfoEditDTO oauthClientInfoEditDTO) {
        oauthClientInfoService.edit(oauthClientInfoEditDTO);
        return R.ok();
    }

    @ApiOperation("编辑状态")
    @GetMapping("/editStatus/{id}")
    @ApiImplicitParam(name = "id", required = true, type = "path")
    public R editStatus(@PathVariable("id") Long id) {
        oauthClientInfoService.editStatus(id);
        return R.ok();
    }

    @ApiOperation("删除")
    @GetMapping("/remove/{id}")
    public R remove(@PathVariable("id") Long id) {
        oauthClientInfoService.removeById(id);
        return R.ok();
    }

}
