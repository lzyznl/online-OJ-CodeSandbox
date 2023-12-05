package com.lzy.yangojcodesandbox.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExecuteCodeRequest {
    /**
     * 程序代码
     */
    private String code;

    /**
     * 程序输入
     */
    private List<String> inputList;

    /**
     * 程序语言类型
     */
    private String codeLanguage;
}
