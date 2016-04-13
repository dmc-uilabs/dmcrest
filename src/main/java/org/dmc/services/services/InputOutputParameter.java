package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InputOutputParameter  {
  
  private String name = null;
  private String type = null;
  private String unit = null;
  private String category = null;
  private BigDecimal value = null;
  private String parameterid = null;

  
  /**
   **/
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   **/
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  
  /**
   **/
  @JsonProperty("unit")
  public String getUnit() {
    return unit;
  }
  public void setUnit(String unit) {
    this.unit = unit;
  }

  
  /**
   **/
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

  
  /**
   **/
  @JsonProperty("value")
  public BigDecimal getValue() {
    return value;
  }
  public void setValue(BigDecimal value) {
    this.value = value;
  }

  
  /**
   **/
  @JsonProperty("parameterid")
  public String getParameterid() {
    return parameterid;
  }
  public void setParameterid(String parameterid) {
    this.parameterid = parameterid;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InputOutputParameter inputOutputParameter = (InputOutputParameter) o;
    return Objects.equals(name, inputOutputParameter.name) &&
        Objects.equals(type, inputOutputParameter.type) &&
        Objects.equals(unit, inputOutputParameter.unit) &&
        Objects.equals(category, inputOutputParameter.category) &&
        Objects.equals(value, inputOutputParameter.value) &&
        Objects.equals(parameterid, inputOutputParameter.parameterid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, unit, category, value, parameterid);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InputOutputParameter {\n");
    
    sb.append("  name: ").append(name).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  unit: ").append(unit).append("\n");
    sb.append("  category: ").append(category).append("\n");
    sb.append("  value: ").append(value).append("\n");
    sb.append("  parameterid: ").append(parameterid).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
