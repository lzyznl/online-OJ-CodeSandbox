package com.lzy.yangojcodesandbox.model.entity;

import lombok.Data;

@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 总程序执行时间
     */
    private Long time;

    /**
     * 单个用例最大运行时间
     */
    private Long maxTime;

    /**
     * 程序执行消耗内存
     */
    private String memory;
}
