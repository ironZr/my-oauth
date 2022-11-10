package com.zr.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {

    private String redirectUri;

    @NotBlank
    private String appId;

}
