package org.dmc.services.projects;

import java.util.Date;
import java.util.Objects;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.member.FollowingMember;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectMember {
    private int profileId;
    private int projectId;
    private boolean accept;
    private int fromProfileId;
    private String from;
    private Date date;
    private String id;

    public ProjectMember() {
    }

    @JsonProperty("id")
    public String getId() {
        fixId();
        return id;
    }

    @JsonProperty("id")
    public void setId(String value) {
        final String[] parts = value.split("-");
        if (parts.length != 3) {
            throw new DMCServiceException(DMCError.IncorrectType, "Project member request id is invalid");
        }
        projectId = Integer.parseInt(parts[0]);
        profileId = Integer.parseInt(parts[1]);
        fromProfileId = Integer.parseInt(parts[2]);
        id = value;
    }

    @JsonProperty("profileId")
    public String getProfileId() {
        return Integer.toString(profileId);
    }

    @JsonProperty("profileId")
    public void setProfileId(String value) {
        profileId = Integer.parseInt(value);
        fixId();
    }

    @JsonProperty("projectId")
    public String getProjectId() {
        return Integer.toString(projectId);
    }

    @JsonProperty("projectId")
    public void setProjectId(String value) {
        projectId = Integer.parseInt(value);
        fixId();
    }

    @JsonProperty("accept")
    public boolean getAccept() {
        return accept;
    }

    @JsonProperty("accept")
    public void setAccept(boolean value) {
        accept = value;
    }

    @JsonProperty("fromProfileId")
    public String getFromProfileId() {
        return Integer.toString(fromProfileId);
    }

    @JsonProperty("fromProfileId")
    public void setFromProfileId(String value) {
        fromProfileId = Integer.parseInt(value);
        fixId();
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("from")
    public void setFrom(String value) {
        from = value;
    }

    @JsonProperty("date")
    public long getDate() {
        return date.getTime();
    }

    @JsonProperty("date")
    public void setDate(long value) {
        date = new Date(value);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ProjectMember projectMember = (ProjectMember) o;
      return Objects.equals(id, projectMember.id) &&
          Objects.equals(profileId, projectMember.profileId) &&
          Objects.equals(projectId, projectMember.projectId) &&
          Objects.equals(accept, projectMember.accept) &&
          Objects.equals(fromProfileId, projectMember.fromProfileId) &&
          Objects.equals(from, projectMember.from) &&
          Objects.equals(date, projectMember.date);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, profileId, projectId, accept, fromProfileId, from, date);
    }

    @Override
    public String toString()  {
      StringBuilder sb = new StringBuilder();
      sb.append("class ProjectMember {\n");
      
      sb.append("  id: ").append(id).append("\n");
      sb.append("  profileId: ").append(profileId).append("\n");
      sb.append("  projectId: ").append(projectId).append("\n");
      sb.append("  accept: ").append(accept).append("\n");
      sb.append("  fromProfileId: ").append(fromProfileId).append("\n");
      sb.append("  from: ").append(from).append("\n");
      sb.append("  date: ").append(date).append("\n");
      sb.append("}\n");
      return sb.toString();
    }
    
    private void fixId()
    {
        id = getProjectId() + "-" + getProfileId() + "-" + getFromProfileId();
    }

}
