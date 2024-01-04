package com.lzy.yangojcodesandbox.codeSandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.lzy.yangojcodesandbox.CodeSandbox;
import com.lzy.yangojcodesandbox.Util.ProcessUtils;
import com.lzy.yangojcodesandbox.model.Enum.JudgeInfoMessageEnum;
import com.lzy.yangojcodesandbox.model.Enum.questionStatusEnum;
import com.lzy.yangojcodesandbox.model.entity.ExecuteCodeRequest;
import com.lzy.yangojcodesandbox.model.entity.ExecuteCodeResponse;
import com.lzy.yangojcodesandbox.model.entity.ExecuteMessage;
import com.lzy.yangojcodesandbox.model.entity.JudgeInfo;
import org.springframework.util.StopWatch;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSandbox implements CodeSandbox {

    private static final String CODE_DIR = "tempCode";

    private static final String USER_CODE_FILE_NAME = "Main.java";

    private static final long waitTime = 5000L;

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        String code = ResourceUtil.readStr("tempCode/unsafeCode/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        List<String> inputList = Arrays.asList("1 2");
        executeCodeRequest.setInputList(inputList);
        executeCodeRequest.setCodeLanguage("Java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
//        List<String> outputList = executeCodeResponse.getOutputList();
//        for(int i=0;i<outputList.size();++i){
//            System.out.println(outputList.get(i));
//        }
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();
        List<String> outputList = new ArrayList<>();
        String codeLanguage = executeCodeRequest.getCodeLanguage();
        long maxProcessTime = 0;

        //1.首次运行程序，没有对应的文件夹则需要创建文件
        String userDir = System.getProperty("user.dir");
        String CodeFirstDir = userDir+ File.separator+CODE_DIR;
        if(!FileUtil.exist(CodeFirstDir)){
            FileUtil.mkdir(CodeFirstDir);
        }
        //2.将用户代码写入到一个文件当中
        String UUIDCodeDir = CodeFirstDir+File.separator+ UUID.randomUUID();
        String UserCodePath = UUIDCodeDir+File.separator+USER_CODE_FILE_NAME;
        File UserCodeFile = FileUtil.writeString(code, UserCodePath, StandardCharsets.UTF_8);

        //3.执行命令编译Java程序
        String CompileCodeCmd = String.format("javac -encoding utf-8 %s",UserCodeFile.getAbsolutePath());
        ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetOutput(CompileCodeCmd);
        if(executeMessage.getExitValue()!=0){
            System.out.println("编译失败");
            //编译失败，后续均无法执行
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            executeCodeResponse.setStatus(questionStatusEnum.FAILED.getValue());
            executeCodeResponse.setMessage(executeMessage.getErrorMessage());
            executeCodeResponse.setJudgeInfo(judgeInfo);
            return executeCodeResponse;
        }else{
            System.out.println("编译成功");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            //运行程序，获取输出
            for(int i=0;i<inputList.size();++i){
                String inputArgs = inputList.get(i);
                String executeCodeCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s",UUIDCodeDir,inputArgs);

                ExecuteMessage execExecuteMessage = ProcessUtils.runProcessAndGetOutput(executeCodeCmd);
                if(execExecuteMessage.getExitValue()!=0){
                    System.out.println("执行失败");
                    JudgeInfo judgeInfo = new JudgeInfo();
                    judgeInfo.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
                    executeCodeResponse.setStatus(questionStatusEnum.FAILED.getValue());
                    executeCodeResponse.setMessage(execExecuteMessage.getErrorMessage());
                    executeCodeResponse.setJudgeInfo(judgeInfo);
                    return executeCodeResponse;
                }else{
                    Long processTime = execExecuteMessage.getProcessTime();
                    if(processTime>maxProcessTime){
                        maxProcessTime=processTime;
                    }
                    outputList.add(execExecuteMessage.getSucceedMessage());
                }
            }
            stopWatch.stop();
            long totalTime = stopWatch.getLastTaskTimeMillis();
            //运行到这里说明程序已经完全正常执行
            JudgeInfo judgeInfo = new JudgeInfo();
            executeCodeResponse.setOutputList(outputList);
            executeCodeResponse.setStatus(questionStatusEnum.SUCCESS.getValue());
            judgeInfo.setMaxTime(maxProcessTime);
            judgeInfo.setTime(totalTime);
            executeCodeResponse.setJudgeInfo(judgeInfo);

            //将执行过程中产生的文件进行删除，防止服务器内存被占满
            if(UserCodeFile.getParent()!=null){
                FileUtil.del(UUIDCodeDir);
            }

            return executeCodeResponse;
        }
    }
}
