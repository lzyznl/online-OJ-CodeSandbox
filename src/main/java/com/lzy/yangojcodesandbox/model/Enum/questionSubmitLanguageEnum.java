package com.lzy.yangojcodesandbox.model.Enum;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 */

public enum questionSubmitLanguageEnum {
    JAVA("Java","Java"),
    CPLUSPLUS("C++","C++"),
    PYTHON("Python","Python"),
    GO("Go","Go");

    private String text;
    private String value;

    questionSubmitLanguageEnum(String text, String value){
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues(){
        List<String> ValuesList = Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
        return ValuesList;
    }

    public static questionSubmitLanguageEnum getEnumByValue(String value){
        if(ObjectUtils.isEmpty(value)){
            return null;
        }
        for(questionSubmitLanguageEnum questionSubmitLanguageEnum:questionSubmitLanguageEnum.values()){
            if(questionSubmitLanguageEnum.value.equals(value)){
                return questionSubmitLanguageEnum;
            }
        }
        return null;
    }

    public String getText(){
        return this.text;
    }

    public String getValue(){
        return this.value;
    }

}
