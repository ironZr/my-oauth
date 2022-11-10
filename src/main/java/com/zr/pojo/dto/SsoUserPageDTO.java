package com.zr.pojo.dto;

import com.zr.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户分页DTO")
@Data
public class SsoUserPageDTO extends PageParam {

    @ApiModelProperty("模糊查询条件")
    private String condition;
}
