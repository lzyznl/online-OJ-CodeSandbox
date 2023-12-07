package com.lzy.yangojcodesandbox.model.Enum;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum questionStatusEnum {
    WAITING("等待中",0),
    RUNNING("判题中",1),
    SUCCESS("成功",2),
    FAILED("失败",3);

    private String text;
    private Integer value;

    questionStatusEnum(String text, Integer value){
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues(){
        List<Integer> ValuesList = Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
        return ValuesList;
    }

    public static questionStatusEnum getEnumByValue(Integer value){
        if(ObjectUtils.isEmpty(value)){
            return null;
        }
        for(questionStatusEnum questionStatusEnum:questionStatusEnum.values()){
            if(questionStatusEnum.value.equals(value)){
                return questionStatusEnum;
            }
        }
        return null;
    }

    public String getText(){
        return this.text;
    }

    public int getValue(){
        return this.value;
    }
}
