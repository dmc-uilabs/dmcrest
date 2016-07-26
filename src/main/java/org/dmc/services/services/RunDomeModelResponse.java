package org.dmc.services.services;

import com.fasterxml.jackson.annotation.JsonProperty;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-07-22T17:42:57.404Z")
public class RunDomeModelResponse  {
  
  private int runId = -999;
 
  @JsonProperty("runId")
  public int getRunId() {
    return runId;
  }
  public void setRunId(int id) {
    this.runId = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RunDomeModelResponse response = (RunDomeModelResponse) o;
    return (response.getRunId()==runId);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n").append("runId: ").append(runId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
