package com.zr.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeVO implements Serializable {

    private String accessToken;

    private String appId;

    private String redirectUri;

}
