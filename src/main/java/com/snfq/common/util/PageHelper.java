package com.snfq.common.util;

import com.snfq.common.json.JsonResult;

public class PageHelper {

	public static void setPage(JsonResult result, Integer pageSize, Integer currentPage, Long totalSize){
		if (CommonUtil.isEmpty(currentPage)) {
			currentPage = 1;
		}
		if (CommonUtil.isEmpty(pageSize)) {
			pageSize = 10;
		}
		result.setCurrentPage(currentPage); // 当前页
		result.setPageSize(pageSize);// 每页显示数量
		result.setTotalSize(totalSize);// 总记录数
		result.setTotalPage((int)((totalSize / pageSize) + (totalSize % pageSize >= 1 ? 1 : 0)));// 总页数
		result.setIsFirstPage(currentPage.intValue() == 1);//是否为第一页
		result.setIsLastPage(result.getTotalPage().intValue() == 0 ? true : currentPage.intValue() == result.getTotalPage().intValue());  //是否为最后一页
		result.setPrePage(currentPage.intValue() == 1 ? 1 : (currentPage.intValue() - 1));//前一页
		result.setNextPage(result.getTotalPage().intValue() == 0 ? 1 : currentPage.intValue() == result.getTotalPage().intValue() ? 
				result.getTotalPage().intValue() : (result.getTotalPage().intValue() - 1));//下一页
		result.setHasNext(!result.getIsLastPage());// 是否有下一页
		result.setHasPre(!result.getIsFirstPage());// 是否有上一页
		result.setSize((int)(totalSize.intValue() == 0 ? 0 : !result.getIsLastPage() ? pageSize : (totalSize % pageSize)));//当前页的数量
	}
}
