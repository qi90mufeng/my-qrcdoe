package com.snfq.common.json;

/**
 * 返回数据封装
 * @author zwp
 *
 */
public class JsonResult {

	// 成功标志
	private boolean success;

	// 错误码
	private String errorCode;

	// 返回的数据对象
	private Object data;

	// 错误信息
	private String errorMsg;
	
	//URL
	private String url;

	/** 分页信息 */
	// 当前页
	private Integer currentPage;
	//当前页的数量
    private Integer size;
	//前一页
    private Integer prePage;
    //下一页
    private Integer nextPage;
    //是否为第一页
    private Boolean isFirstPage;
    //是否为最后一页
    private Boolean isLastPage;
    
	// 每页显示数量
	private Integer pageSize;

	// 总页数
	private Integer totalPage;

	// 总记录数
	private Long totalSize;

	// 是否有下一页
	private Boolean hasNext;

	// 是否有上一页
	private Boolean hasPre;

		
	public JsonResult() {
	}

	public JsonResult(Object data) {
		if (data != null) {
			this.data = data;
		}
		this.success = true;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPrePage() {
		return prePage;
	}

	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public Boolean getHasNext() {
		return hasNext;
	}

	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Boolean getHasPre() {
		return hasPre;
	}

	public void setHasPre(Boolean hasPre) {
		this.hasPre = hasPre;
	}

	@Override
	public String toString() {
		return "JsonResult [success=" + success + ", errorCode=" + errorCode
				+ ", data=" + data + ", errorMsg=" + errorMsg
				+ ",currentPage=" + currentPage + ", size=" + size + "]";
	}
}
