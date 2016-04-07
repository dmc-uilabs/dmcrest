package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class InterfaceInParams  {
  
  private InputOutputParameter inputName3 = null;
  private InputOutputParameter inputName4 = null;
  private InputOutputParameter inputName1 = null;
  private InputOutputParameter inputName2 = null;

  
  /**
   **/
  @JsonProperty("inputName3")
  public InputOutputParameter getInputName3() {
    return inputName3;
  }
  public void setInputName3(InputOutputParameter inputName3) {
    this.inputName3 = inputName3;
  }

  
  /**
   **/
  @JsonProperty("inputName4")
  public InputOutputParameter getInputName4() {
    return inputName4;
  }
  public void setInputName4(InputOutputParameter inputName4) {
    this.inputName4 = inputName4;
  }

  
  /**
   **/
  @JsonProperty("inputName1")
  public InputOutputParameter getInputName1() {
    return inputName1;
  }
  public void setInputName1(InputOutputParameter inputName1) {
    this.inputName1 = inputName1;
  }

  
  /**
   **/
  @JsonProperty("inputName2")
  public InputOutputParameter getInputName2() {
    return inputName2;
  }
  public void setInputName2(InputOutputParameter inputName2) {
    this.inputName2 = inputName2;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterfaceInParams interfaceInParams = (InterfaceInParams) o;
    return Objects.equals(inputName3, interfaceInParams.inputName3) &&
        Objects.equals(inputName4, interfaceInParams.inputName4) &&
        Objects.equals(inputName1, interfaceInParams.inputName1) &&
        Objects.equals(inputName2, interfaceInParams.inputName2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputName3, inputName4, inputName1, inputName2);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterfaceInParams {\n");
    
    sb.append("  inputName3: ").append(inputName3).append("\n");
    sb.append("  inputName4: ").append(inputName4).append("\n");
    sb.append("  inputName1: ").append(inputName1).append("\n");
    sb.append("  inputName2: ").append(inputName2).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
