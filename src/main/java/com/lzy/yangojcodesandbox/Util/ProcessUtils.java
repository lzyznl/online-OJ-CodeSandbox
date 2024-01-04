package com.lzy.yangojcodesandbox.Util;

import cn.hutool.core.util.RuntimeUtil;
import com.lzy.yangojcodesandbox.model.entity.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author lzy
 * 编译运行用户提交程序获取输出相关类
 */
public class ProcessUtils {

    private static final long waitTime = 5000L;

    public static ExecuteMessage runProcessAndGetOutput(String cmd){
        ExecuteMessage executeMessage = new ExecuteMessage();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Process execProcess = RuntimeUtil.exec(cmd);
        //进行时间安全控制,超过时间阈值，则销毁

        new Thread(()->{
            try {
                Thread.sleep(waitTime);
                System.out.println("程序超时，自动中止");
                execProcess.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            int exitValue = execProcess.waitFor();
            stopWatch.stop();
            long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
            executeMessage.setProcessTime(lastTaskTimeMillis);
            executeMessage.setExitValue(exitValue);
            if(exitValue==0){
                //命令正常结束，获取成功输出
                BufferedReader successBufferedReader = new BufferedReader(new InputStreamReader(execProcess.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder successOutputBuilder = new StringBuilder();
                String line = "";
                while((line = successBufferedReader.readLine())!=null){
                    successOutputBuilder.append(line);
                }
                executeMessage.setSucceedMessage(successOutputBuilder.toString());
            }else{
                //命令非正常结束，获取报错信息
                BufferedReader failedBufferedReader = new BufferedReader(new InputStreamReader(execProcess.getErrorStream(),StandardCharsets.UTF_8));
                StringBuilder failedStringBuilder = new StringBuilder();
                String Line = "";
                while((Line=failedBufferedReader.readLine())!=null){
                    failedStringBuilder.append(Line);
                }
                executeMessage.setErrorMessage(failedStringBuilder.toString());
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return executeMessage;
    }
}
