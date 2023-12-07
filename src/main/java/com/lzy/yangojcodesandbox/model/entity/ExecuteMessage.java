package com.lzy.yangojcodesandbox.model.entity;

import lombok.Data;

/**
 * 程序执行相关信息类
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String succeedMessage;

    private String errorMessage;

    private Long processTime;

}
