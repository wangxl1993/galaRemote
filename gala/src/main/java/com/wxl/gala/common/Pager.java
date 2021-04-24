package com.wxl.gala.common;

import java.io.Serializable;

/**
 * 分页对象
 * @author helen
 *
 */
public class Pager implements Serializable{

	private Integer page;//当前页面
	private Integer rows;//每页记录数
	private Integer offset;
	
	public void setPagerParams(){
		this.offset = (this.page - 1) * rows;
	}

	public Pager() {
		super();
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
