package com.zr.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OauthAppInfoEditDTO implements Serializable {

    @NotNull
    private Long id;

    /**
     * 客户端名称
     */
    @NotBlank
    private String appName;

    /**
     * 回调地址
     */
    @NotBlank
    private String appUri;


    /**
     * 公司名称
     */
    @NotBlank
    private String companyName;

    /**
     * 描述信息
     */
    @NotBlank
    private String description;

}
