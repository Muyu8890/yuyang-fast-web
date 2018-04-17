package com.jenphy.config;

import com.jenphy.base.Result;
import com.jenphy.constants.BaseEnums;
import com.jenphy.exception.BaseException;
import com.jenphy.exception.BusinessException;
import com.jenphy.exception.ErrorException;
import com.jenphy.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 * Created by zhuang on 2018/4/16.
 */
@RestControllerAdvice
public class GlobalExceptionConfig {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    /*
     * 处理 BusinessException 异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleServiceException(BusinessException e){
        Result result = ResultUtils.failure(e.getCode(), e.getDesc());
        result.setStatus(HttpStatus.OK.value());
        logger.info("BusinessException[code: {}, desc: {}]", e.getCode(), e.getDesc());
        return result;
    }

    /*
     * 处理 NoHandlerFoundException 异常.
     * 需配置 [spring.mvc.throw-exception-if-no-handler-found=true]
     * 需配置 [spring.resources.add-mappings=false]
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNotFoundException(NoHandlerFoundException e){
        Result result = ResultUtils.failure(BaseEnums.NOT_FOUND.code(), BaseEnums.NOT_FOUND.desc());
        result.setStatus(HttpStatus.NOT_FOUND.value());
        logger.info(e.getMessage());
        return result;
    }

    /*
     * 处理 ErrorException 异常
     */
    @ExceptionHandler(ErrorException.class)
    public Result handleErrorException(BaseException e){
        Result result = ResultUtils.failure(e.getCode(), e.getDesc());
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("ErrorException[code: {}, desc: {}]", e.getCode(), e.getDesc(), e);
        return result;
    }

    /*
     * 处理 Exception 异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        Result result = ResultUtils.failure(BaseEnums.ERROR.code(), BaseEnums.ERROR.desc());
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error(e.getMessage(), e);
        return result;
    }
}
