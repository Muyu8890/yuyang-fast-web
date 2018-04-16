package com.jenphy.base;

import com.jenphy.util.ResultUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

	public static Logger logger = Logger.getLogger(BaseController.class);


	@Autowired
	public HttpServletRequest request;

	@Autowired
	public HttpServletResponse response;

	public Result result(){
		return ResultUtils.success();
	}
	public Result result(Object result){
		return ResultUtils.successWithData(result);
	}

}
