package com.jenphy.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePage implements Pageable {
	public static Logger logger = LoggerFactory.getLogger(BasePage.class);
	
	public static final int DEF_PAGE_SIZE = 20;
	public static final int DEF_PAGE_NO = 1;

	
	protected int pageNo = DEF_PAGE_NO;
	protected int pageSize = DEF_PAGE_SIZE;

	protected int totalCount = 0;
	
	
	public static int cpn(Integer pageNo) {
		return (pageNo == null || pageNo < 1) ? 1 : pageNo;
	}

	public BasePage() {
	}

	public BasePage(int pageNo, int pageSize, int totalCount) {
		setTotalCount(totalCount);
		setPageSize(pageSize);
		setPageNo(pageNo);
		adjustPageNo();
	}

	public void adjustPageNo() {
		if (pageNo == 1) {
			return;
		}
		int tp = getTotalPage();
		if (pageNo > tp) {
			pageNo = tp;
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		int totalPage = totalCount / pageSize;
		if (totalPage == 0 || totalCount % pageSize != 0) {
			totalPage++;
		}
		return totalPage;
	}

	public boolean isFirstPage() {
		return pageNo <= 1;
	}

	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}

	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	public void setTotalCount(int totalCount) {
		if (totalCount < 0) {
			this.totalCount = 0;
		} else {
			this.totalCount = totalCount;
		}
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			this.pageSize = DEF_PAGE_SIZE;
		} else {
			this.pageSize = pageSize;
		}
	}

	public void setPageNo(int pageNo) {
		if (pageNo < 1) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
	}

}
