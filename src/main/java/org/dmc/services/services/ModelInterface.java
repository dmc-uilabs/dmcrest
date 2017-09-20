package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ModelInterface  {
  
  private Map<String, DomeModelParam> inParams = new HashMap<String, DomeModelParam>();
  private Map<String, DomeModelParam> outParams = new HashMap<String, DomeModelParam>();
  
  public void addParameter(Boolean isInput, DomeModelParam param) {
	  if(isInput) {
		  inParams.put(param.getName(), param);
	  } else {
		  outParams.put(param.getName(), param);
	  }
  }
  
  /**
   **/
  @JsonProperty("inParams")
  public Map<String, DomeModelParam> getInParams() {
    return inParams;
  }
  public void setInParams(Map<String, DomeModelParam> inParams) {
    this.inParams = inParams;
  }

  
  /**
   **/
  @JsonProperty("outParams")
  public Map<String, DomeModelParam> getOutParams() {
    return outParams;
  }
  public void setOutParams(Map<String, DomeModelParam> outParams) {
    this.outParams = outParams;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelInterface _interface = (ModelInterface) o;
    return Objects.equals(inParams, _interface.inParams) &&
        Objects.equals(outParams, _interface.outParams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inParams, outParams);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelInterface {\n");
    
    sb.append("  inParams: ").append(inParams.toString()).append("\n");
    sb.append("  outParams: ").append(outParams.toString()).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
