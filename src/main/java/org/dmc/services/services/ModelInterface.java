package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ModelInterface  {
  
  private InterfaceInParams inParams = null;
  private InterfaceOutParams outParams = null;

  
  /**
   **/
  @JsonProperty("inParams")
  public InterfaceInParams getInParams() {
    return inParams;
  }
  public void setInParams(InterfaceInParams inParams) {
    this.inParams = inParams;
  }

  
  /**
   **/
  @JsonProperty("outParams")
  public InterfaceOutParams getOutParams() {
    return outParams;
  }
  public void setOutParams(InterfaceOutParams outParams) {
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
    
    sb.append("  inParams: ").append(inParams).append("\n");
    sb.append("  outParams: ").append(outParams).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
