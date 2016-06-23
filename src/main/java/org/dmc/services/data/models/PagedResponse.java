package org.dmc.services.data.models;

import java.util.List;

public class PagedResponse {

	private Long count;
	private List<? extends BaseModel> data;
	
	public PagedResponse(Long count, List<? extends BaseModel> data) {
		this.count = count;
		this.data = data;
	}
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<? extends BaseModel> getData() {
		return data;
	}
	public void setData(List<? extends BaseModel> data) {
		this.data = data;
	}
	
}
