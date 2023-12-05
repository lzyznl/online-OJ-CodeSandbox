package com.lzy.yangojcodesandbox.model;

import lombok.Data;

import java.util.List;

@Data
public class ExecuteCodeResponse {
    private List<String> outputList;
    private Integer status;
    private String message;
    private JudgeInfo judgeInfo;
}
