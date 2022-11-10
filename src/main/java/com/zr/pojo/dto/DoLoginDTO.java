package com.zr.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class DoLoginDTO implements Serializable {

    @NotBlank
    private String account;

    @NotBlank
    private String password;

    private String redirectUri;

    @NotBlank
    private String appId;

}
