package com.zr.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("添加用户DTO")
public class SsoUserAddDTO implements Serializable {

    @ApiModelProperty(value = "登录名", required = true)
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

}
