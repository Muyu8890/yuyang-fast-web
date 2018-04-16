package com.jenphy.page;


public interface Pageable {

	int getTotalCount();
	
	int getTotalPage();

	int getPageSize();

	int getPageNo();

	boolean isFirstPage();
	
	boolean isLastPage();
	
	int getPrePage();
	
}
