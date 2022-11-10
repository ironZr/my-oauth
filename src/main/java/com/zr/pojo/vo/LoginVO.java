package com.zr.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginVO implements Serializable {

    //给前端重定向唯一标识
    private Integer code = 302;

    private SsoUserVO ssoUserVO;

    private String redirectUri;

    private String appId;

    private String accessToken;
}
