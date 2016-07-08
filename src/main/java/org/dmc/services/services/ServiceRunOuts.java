package org.dmc.services.services;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class ServiceRunOuts  {
  private ArrayList<ServiceRunOut> outputs;
  public ServiceRunOuts() {
	  outputs = new ArrayList<ServiceRunOut>();
  }
  
  @JsonProperty("outputs")  
public ArrayList<ServiceRunOut> getOutputs() {
	return outputs;
}
public void setOutputs(ArrayList<ServiceRunOut> outputs) {
	this.outputs = outputs;
}
 
  public void add(ServiceRunOut r)
  {
	  outputs.add(r);
  }
  public ServiceRunOut get(int i)
  {
	  return outputs.get(i);
  }
  public int size()
  {
	  return outputs.size();
  }
}
