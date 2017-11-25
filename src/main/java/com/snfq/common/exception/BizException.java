package com.snfq.common.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer errorCode = 502;
	
	public BizException() {
		super();
	}

	public BizException(String msg, Integer errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

	public BizException(RuntimeException e) {
		super(e);
	}

	public BizException(Throwable e) {
		super(e);
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
}
