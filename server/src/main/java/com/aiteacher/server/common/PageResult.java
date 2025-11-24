package com.aiteacher.server.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分页响应结果
 *
 * @author AI Teacher Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<List<T>> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer pages;

    public PageResult() {
        super();
    }

    public PageResult(List<T> list, Long total, Integer page, Integer size) {
        super(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), list);
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }

    /**
     * 成功分页响应
     */
    public static <T> PageResult<T> success(List<T> list, Long total, Integer page, Integer size) {
        return new PageResult<>(list, total, page, size);
    }

    /**
     * 空分页响应
     */
    public static <T> PageResult<T> empty(Integer page, Integer size) {
        return new PageResult<>(List.of(), 0L, page, size);
    }
}

