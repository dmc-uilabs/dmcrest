package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.ArrayList;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InterfaceInParams  {
  
	private ArrayList<InputOutputParameter> inPars=new ArrayList<InputOutputParameter>();
  /*private InputOutputParameter inputName3 = null;
  private InputOutputParameter inputName4 = null;
  private InputOutputParameter inputName1 = null;
  private InputOutputParameter inputName2 = null;*/

//  public InterfaceInParams(ArrayList<InParameter> in)
//  {
//	  for (int i=0;i<inPars.size();i++)
//	  {
//		  InputOutputParameter p = new  InputOutputParameter();
//		  p.setCategory(in.get(i).getCategory());
//		  
//	  }
//  }
	
  /**
   **/
  @JsonProperty("inputName3")
  public InputOutputParameter getInputName3() {
	  if (inPars.size()>2)
		  return inPars.get(2);
	  else return null;
  }
  public void setInputName3(InputOutputParameter inputName3) {
    this.inPars.set(2, inputName3);
  }

  
  /**
   **/
  @JsonProperty("inputName4")
  public InputOutputParameter getInputName4() {
	  if (inPars.size()>3)
		  return inPars.get(3);
	  else return null;
  }
  public void setInputName4(InputOutputParameter inputName4) {
	  this.inPars.set(3, inputName4);
  }

  
  /**
   **/
  @JsonProperty("inputName1")
  public InputOutputParameter getInputName1() {
	  if (inPars.size()>0)
		  return inPars.get(0);
	  else return null;
  }
  public void setInputName1(InputOutputParameter inputName1) {
	  this.inPars.set(0, inputName1);
  }

  
  /**
   **/
  @JsonProperty("inputName2")
  public InputOutputParameter getInputName2() {
	  if (inPars.size()>1)
		  return inPars.get(1);
	  else return null;
  }
  public void setInputName2(InputOutputParameter inputName2) {
	  this.inPars.set(1, inputName2);
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
    for (int i=0;i<inPars.size();i++)
    	result = result && Objects.equals(inPars.get(i), interfaceOutParams.getOutPars().get(i));
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(inPars); 
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterfaceOutParams {\n");
    for (int i=0;i<inPars.size();i++)
    {
    	sb.append("  outputName").append(i).append(": ").append(inPars.get(i)).append("\n");
    }
    sb.append("}\n");
    return sb.toString();
  }
}
