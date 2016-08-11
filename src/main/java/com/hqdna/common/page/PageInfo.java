package com.hqdna.common.page;

/**
 * easyui的datagrid向后台传递参数使用的model
 * 
 * @author Joey
 *
 *         2014-7-29下午1:51:25
 */
public class PageInfo implements java.io.Serializable {
	private static final long serialVersionUID = -3591536968171663992L;
	private int currentPage = 1;// 当前页
	private int rowsOnePage = 10;// 每页显示记录数
	private int startIndex = 0;// 开始记录的索引
	private String sort = null;// 排序字段名
	private String order = "asc";// 按什么排序(asc,desc)

	public int getPage() {
		return currentPage;
	}

	public void setPage(int currentPage) {
		this.currentPage = currentPage;
		this.startIndex = (currentPage - 1) * rowsOnePage;
	}

	public int getRows() {
		return rowsOnePage;
	}

	public void setRows(int rowsOnePage) {
		this.rowsOnePage = rowsOnePage;
		this.startIndex = (currentPage - 1) * rowsOnePage;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getStartIndex() {
		return startIndex;
	}

}
