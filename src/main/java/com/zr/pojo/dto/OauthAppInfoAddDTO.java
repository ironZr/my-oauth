package com.zr.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OauthAppInfoAddDTO implements Serializable {

    /**
     * 客户端名称
     */
    @NotBlank
    private String appName;

    /**
     * 客户端index地址
     */
    @NotBlank
    private String appUri;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 公司名称
     */
    @NotBlank
    private String companyName;
}
