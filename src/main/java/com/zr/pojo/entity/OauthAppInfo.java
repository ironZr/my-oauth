package com.zr.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: sy-sso
 * @description: 接入的客户端信息表
 * @author: zhangRui
 * @create: 2022-11-01 22:58
 **/
@Data
public class OauthAppInfo implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 接入的客户端ID
     */
    private String appId;

    /**
     * 客户端名称
     */
    private String appName;

    /**
     * 接入的客户端的密钥
     */
    private String appSecret;

    /**
     * 客户端index地址
     */
    private String appUri;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 0表示未开通；1表示正常使用；2表示已被禁用
     */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String createUser;

    private String updateUser;


}
