package com.zr.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PageParam {
    @ApiModelProperty("页数")
    @Range(min = 1, message = "当前页不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty("显示行数")
    @Range(min = 1, max = 10000, message = "显示行数范围[10,20]")
    private Integer rows = 10;
}
