package com.jenphy.exception;

public class BusinessException extends RuntimeException implements BaseException {

    private Enum errorEnum;
    private String code;
    private String desc;
    private Object errorResult;

    public BusinessException(Enum errorEnum){
        ExceptionUtils.setExceptionParam(this, errorEnum);
    }

    public BusinessException(Enum errorEnum, Exception e){
        super(e);
        ExceptionUtils.setExceptionParam(this, errorEnum, e);
    }
    public BusinessException(Enum errorEnum, Object errorResult){
        ExceptionUtils.setExceptionParam(this, errorEnum, errorResult);
    }
    public BusinessException(Enum errorEnum, Exception e, Object errorResult){
        super(e);
        ExceptionUtils.setExceptionParam(this, errorEnum, errorResult);
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Object getErrorResult() {
        return errorResult;
    }

    @Override
    public Enum getErrorEnum() {
        return errorEnum;
    }

    @Override
    public BusinessException setErrorEnum(Enum errorEnum) {
        this.errorEnum = errorEnum;
        return this;
    }

    @Override
    public BusinessException setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public BusinessException setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public BusinessException setErrorResult(Object errorResult) {
        this.errorResult = errorResult;
        return this;
    }

}
