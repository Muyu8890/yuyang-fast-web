package com.jenphy.exception;

public class ExceptionUtils {
    static void setExceptionParam(BaseException baseException, Enum errorEnum) {
        try {
            baseException.setCode((String) errorEnum.getClass().getDeclaredField("code").get(errorEnum));
            baseException.setDesc((String) errorEnum.getClass().getDeclaredField("desc").get(errorEnum));
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
    }

    static void setExceptionParam(BaseException baseException, Enum errorEnum, Object errorResult) {
        setExceptionParam(baseException, errorEnum);
        baseException.setErrorResult(errorResult);
    }
}
