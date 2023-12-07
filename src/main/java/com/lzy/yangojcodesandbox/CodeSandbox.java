package com.lzy.yangojcodesandbox;


import com.lzy.yangojcodesandbox.model.entity.ExecuteCodeRequest;
import com.lzy.yangojcodesandbox.model.entity.ExecuteCodeResponse;

/**
 * @author lzy
 */
public interface CodeSandbox {

    /**
     * 代码沙箱通用接口类
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
