package com.zr.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LogoutDTO implements Serializable {

    @NotBlank
    private String appId;

    @NotBlank
    private String accessToken;

}
