package com.jenphy.query;


import com.jenphy.page.BasePage;

import java.util.List;

public class Page<T> extends BasePage implements java.io.Serializable {

	private static final long serialVersionUID = 80238317179585389L;

	private List<T> data;


	public Page(int pageNo, int pageSize, int totalCount, List<T> data) {
		super(pageNo, pageSize, totalCount);
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Page{" +
				"pageNo=" + pageNo +
				", pageSize=" + pageSize +
				", totalCount=" + totalCount +
				", data=" + data +
				'}';
	}
}
