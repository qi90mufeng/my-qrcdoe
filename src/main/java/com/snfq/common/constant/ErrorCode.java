package com.snfq.common.constant;

/**
 * 提供对外接口的返回错误码
 * @author fujin
 * @version $Id: CustomerSaveDelegate.java, v 0.1 2017-10-11 10:11 Exp $$
 */
public enum ErrorCode {
	
	SUCCESS(200, "正常"),
	ERROR_SYS(505, "系统错误"),
	ERROR_LOGIN_OUT(404, "连接远程服务异常"),
	ERROR_FUNCTION_NOTSUPPORT(1001, "暂不支持该功能"),
	ERROR_USER_NOEXIST(5001,"用户不存在"),
	ERROR_PARAM_LOSE(6001, "缺少参数"),		//参数必填
	ERROR_PARAM_NULL(6002, "参数为空"),        //参数为空
	ERROR_PARAM_ILLEGE(6003, "参数不合法"),    //解析json字符串异常、参数必须为数字（字母）、
	ERROR_BUSINESS(1002, "");             //业务问题,比如启动流程，未生成相应的任务信息、（业务失败）
	
	private Integer errorCode;
	private String errorName;
	
	private ErrorCode(Integer errorCode, String errorName) {
		this.errorCode = errorCode;
		this.errorName = errorName;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

}
