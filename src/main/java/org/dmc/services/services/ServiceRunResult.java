package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRunResult  {
  
	public static final int RUNNING=0;
	public static final int COMPLETE=1;
	public static final int NOTSET=-1;
	
	private ServiceRunOuts outs;
	private int status;
	
	public ServiceRunResult()
	{
		outs=null;
		status=ServiceRunResult.NOTSET;
	}
	
	@JsonProperty("outs")
	public ServiceRunOuts getOuts() {
		return outs;
	}
	public void setOuts(ServiceRunOuts os) {
		this.outs = os;
	}
	@JsonProperty("status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
