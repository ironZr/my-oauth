package com.zr.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 已登录用户信息
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SsoUserVO implements Serializable {

	private static final long serialVersionUID = 1764365572138947234L;

    private String account;

    private String username;

}
