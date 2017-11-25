package com.snfq.common.constant;

/**
 * @author fujin
 * @version $Id: CustomerSaveDelegate.java, v 0.1 2017-10-11 10:11 Exp $$
 */
public enum PlatType {
	SINAFENQ_PLAT("SINAFENQ", "新浪分期"),
	XJD_PLAT("XJD", "现金贷");

	private String platNo;
	private String platName;
	
	private PlatType(String platNo, String platName) {
		this.platNo = platNo;
		this.platName = platName;
	}

	public String getPlatNo() {
		return platNo;
	}

	public String getPlatName() {
		return platName;
	}
}
