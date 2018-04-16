package com.jenphy.exception;


public class ErrorException extends RuntimeException implements BaseException {
    private Enum errorEnum;
    private String code;
    private String desc = "系统异常";
    private Object errorResult;

    public ErrorException(){
    }
    public ErrorException(Enum errorEnum){
        ExceptionUtils.setExceptionParam(this, errorEnum);
    }

    public ErrorException(Enum errorEnum, Exception e){
        super(e);
        ExceptionUtils.setExceptionParam(this, errorEnum);
    }
    public ErrorException(Enum errorEnum, Object errorResult){
        ExceptionUtils.setExceptionParam(this, errorEnum, errorResult);
    }
    public ErrorException(Enum errorEnum, Exception e, Object errorResult){
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
    public ErrorException setErrorEnum(Enum errorEnum) {
        this.errorEnum = errorEnum;
        return this;
    }

    @Override
    public ErrorException setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public ErrorException setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public ErrorException setErrorResult(Object errorResult) {
        this.errorResult = errorResult;
        return this;
    }
}
