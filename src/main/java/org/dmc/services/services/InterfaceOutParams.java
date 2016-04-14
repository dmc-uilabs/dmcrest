package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InterfaceOutParams  {
  
  private InputOutputParameter outputName1 = null;
  private InputOutputParameter outputName2 = null;

  
  /**
   **/
  @JsonProperty("outputName1")
  public InputOutputParameter getOutputName1() {
    return outputName1;
  }
  public void setOutputName1(InputOutputParameter outputName1) {
    this.outputName1 = outputName1;
  }

  
  /**
   **/
  @JsonProperty("outputName2")
  public InputOutputParameter getOutputName2() {
    return outputName2;
  }
  public void setOutputName2(InputOutputParameter outputName2) {
    this.outputName2 = outputName2;
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
    return Objects.equals(outputName1, interfaceOutParams.outputName1) &&
        Objects.equals(outputName2, interfaceOutParams.outputName2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outputName1, outputName2);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InterfaceOutParams {\n");
    
    sb.append("  outputName1: ").append(outputName1).append("\n");
    sb.append("  outputName2: ").append(outputName2).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
