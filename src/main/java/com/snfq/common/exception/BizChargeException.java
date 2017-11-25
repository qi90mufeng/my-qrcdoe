package com.snfq.common.exception;

public class BizChargeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BizChargeException() {
		super();
	}

	public BizChargeException(String msg) {
		super(msg);
	}

	public BizChargeException(RuntimeException e) {
		super(e);
	}

	public BizChargeException(Throwable e) {
		super(e);
	}
}
