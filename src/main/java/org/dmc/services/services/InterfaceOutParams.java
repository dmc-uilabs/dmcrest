package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InterfaceOutParams  {
  
  private ArrayList<InputOutputParameter> outPars=new ArrayList<InputOutputParameter>();
  
  /**
   **/
  @JsonProperty("outputName1")
  public InputOutputParameter getOutputName1() {
	  if (outPars.size()>0)
		  return outPars.get(0);
	  else return null;
  }
  public void setOutputName1(InputOutputParameter outputName1) {
	  outPars.add(0, outputName1);
  }
  
  public ArrayList<InputOutputParameter> getOutPars()
  {
	  return outPars;
  }
  
  /**
   **/
  @JsonProperty("outputName2")
  public InputOutputParameter getOutputName2() {
	  if (outPars.size()>1)
		  return outPars.get(1);
	  else return null;
  }
  public void setOutputName2(InputOutputParameter outputName2) {
	  outPars.add(1, outputName2);
  }
  @JsonProperty("outputName")
  public InputOutputParameter getOutputName(int n) {
	  if (outPars.size()>n-1)
		  return outPars.get(n-1);
	  else return null;
  }
  public void setOutputName(InputOutputParameter outputNamen) {
	  outPars.add(outputNamen);
  }
  public void setOutputName(int n, InputOutputParameter outputNamen) {
	  outPars.add(n, outputNamen);
  }
 
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterfaceOutParams interfaceOutParams = (InterfaceOutParams) o;
    Boolean result=true;
    for (int i=0;i<outPars.size();i++)
    	result = result && Objects.equals(outPars.get(i), interfaceOutParams.getOutPars().get(i));
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(outPars); 
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterfaceOutParams {\n");
    for (int i=0;i<outPars.size();i++)
    {
    	sb.append("  outputName").append(i).append(": ").append(outPars.get(i)).append("\n");
    }
    sb.append("}\n");
    return sb.toString();
  }
}
