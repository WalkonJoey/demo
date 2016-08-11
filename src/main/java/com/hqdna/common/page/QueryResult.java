package com.hqdna.common.page;

import java.util.List;

/**
 * 查询结果封装类
 * @author limq
 * 2013-8-9 下午04:54:23
 * @param <T>
 */
public class QueryResult<T> {
	private List<T> resultlist;
	private long totalrecord;

	public List<T> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}

}
