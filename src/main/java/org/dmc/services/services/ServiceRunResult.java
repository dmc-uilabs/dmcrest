package org.dmc.services.services;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRunResult  {

	public static final int RUNNING=0;
	public static final int COMPLETE=1;
  public static final int CANCELLED=2;
	public static final int NOTSET=-1;

	private Map<String, DomeModelParam> outParams=new HashMap<String, DomeModelParam>();
	private int status;

	public ServiceRunResult()
	{
		status=ServiceRunResult.NOTSET;
	}

	@JsonProperty("outParams")
	public Map<String, DomeModelParam> getOutParams() {
		return outParams;
	}
	public void setOuts(Map<String, DomeModelParam> os) {
		this.outParams = os;
	}
	@JsonProperty("status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
