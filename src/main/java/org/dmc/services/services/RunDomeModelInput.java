package org.dmc.services.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-07-22T17:42:57.404Z")
public class RunDomeModelInput  {
  
  private String serviceId = "";
  private Map<String, DomeModelParam> inParams=new HashMap<String, DomeModelParam>();
  private Map<String, DomeModelParam> outParams=new HashMap<String, DomeModelParam>();
 
  @JsonProperty("serviceId")
  public String getServiceId() {
    return serviceId;
  }
  public void setServiceId(String id) {
    this.serviceId = id;
  }
  
  @JsonProperty("inParams")
  public Map<String, DomeModelParam> getInParams() {
	    return inParams;
	  }
  public void setInParams(Map<String, DomeModelParam> inP) {
	    this.inParams = inP;
	  }

  @JsonProperty("outParams")
  public Map<String, DomeModelParam> getOutParams() {
	    return outParams;
	  }
  public void setOutParams(Map<String, DomeModelParam> outP) {
	    this.outParams = outP;
	  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    
    RunDomeModelInput response = (RunDomeModelInput) o;
    
    boolean paras = true;
    paras = paras && (inParams.size()==response.getInParams().size()) && (outParams.size()==response.getOutParams().size());
    
    if (paras)
    {   
    	//for input variables
    	Iterator it = inParams.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		paras = paras && (pair.getKey().equals(response.getInParams().get(pair.getKey())));
    	}
    	//for output variables
    	Iterator ot = inParams.entrySet().iterator();
    	while (ot.hasNext()) {
    		Map.Entry pair = (Map.Entry)ot.next();
    		paras = paras && (pair.getKey().equals(response.getInParams().get(pair.getKey())));
    	}
    }
    return (paras && (response.getServiceId()==serviceId));
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n").append("serviceId: ").append(serviceId).append(",\n");
    
    sb.append("inParams: { \n");
    Iterator it = inParams.entrySet().iterator();
	while (it.hasNext()) {
		Map.Entry pair = (Map.Entry)it.next();
		sb.append(pair.getKey().toString()).append(": ").append(pair.getValue().toString()).append(",\n");
	}
	sb.append("}, \n");
	
	sb.append("outParams: { \n");
    Iterator ot = inParams.entrySet().iterator();
	while (ot.hasNext()) {
		Map.Entry pair = (Map.Entry)ot.next();
		sb.append(pair.getKey().toString()).append(": ").append(pair.getValue().toString()).append(",\n");
	}
	sb.append("}, \n");
    sb.append("}\n");
    return sb.toString();
  }
}
