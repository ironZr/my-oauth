package com.zr.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("分页数据封装")
public class PageResult<T> implements Serializable {
    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long totalPage;

    @ApiModelProperty("当前页数据")
    private List<T> items;

    public static <T> PageResult<T> empty() {
        return PageResult.<T>builder().total(0L).totalPage(0L).items(Collections.emptyList()).build();
    }

    public PageResult(Page page) {
        this.total = page.getTotal();
        this.totalPage = page.getPages();
        this.items = page.getRecords();
    }
}