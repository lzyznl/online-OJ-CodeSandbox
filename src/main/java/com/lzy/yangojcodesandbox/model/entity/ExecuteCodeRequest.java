package com.lzy.yangojcodesandbox.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
