package org.dmc.services.services;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ServiceRunOut  {
  private String value = null;
  private String parameterid = null;
  
  @JsonProperty("value") 
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
@JsonProperty("parameterid")
public String getParameterid() {
	return parameterid;
}
public void setParameterid(String parameterid) {
	this.parameterid = parameterid;
}

}
