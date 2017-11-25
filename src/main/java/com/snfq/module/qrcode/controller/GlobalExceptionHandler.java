package com.snfq.module.flow.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.snfq.common.exception.BizException;
import com.snfq.common.exception.LoginTimeoutException;
import com.snfq.common.exception.SecretException;
import com.snfq.common.json.JsonResult;

/**
 * 全局异常处理类
 * @author zwp
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseBody
	public JsonResult jsonErrorHandler(HttpServletRequest req, Exception ex)
			throws Exception {
		if (ex != null) {
			if (ex instanceof javax.servlet.ServletException
					|| ex instanceof ModelAndViewDefiningException) {
				ex = new RuntimeException("resource not found");
				return outputError(req, "404", ex);
			} else if (ex instanceof LoginTimeoutException) {
				return outputError(req, "501", ex);
			} else if (ex instanceof SecretException) {
				return outputError(req, "503", ex);
			} else if (ex instanceof BizException) {
				return outputError(req, String.valueOf(((BizException) ex).getErrorCode()), ex);
			} else {
				return outputError(req, "500", ex);
			}
		}
		return null;
	}
	
	private JsonResult outputError(HttpServletRequest req, String errorCode,
			Exception ex) {
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		result.setErrorCode(errorCode);
		if (ex != null && ex.getMessage() != null && ex instanceof Exception) {
			result.setErrorMsg(ex.getMessage());
		}
		result.setUrl(req.getRequestURL().toString());
		return result;
	}
}
