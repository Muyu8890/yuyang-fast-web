package com.jenphy.exception;

/**
 * 基础异常类
 * Created by zhuang on 2018/4/12.
 */
public interface BaseException {

    Enum getErrorEnum();

    BaseException setErrorEnum(Enum errorEnum);

    String getCode();

    BaseException setCode(String code);

    String getDesc();

    BaseException setDesc(String desc);

    Object getErrorResult();

    BaseException setErrorResult(Object errorResult);
}

