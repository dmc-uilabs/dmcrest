package org.dmc.services.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class UserAccount  {
    
    private String id = new String();
    private String companyId = new String();
    private String profileId = new String();
    private String firstName = new String();
    private String lastName = new String();
    private String displayName = new String();
    private String email = new String();
    private Boolean deactivated = true;
    private String location = new String();
    private String timezone = new String();
    private UserAccountPrivacy privacy = new UserAccountPrivacy();
    
//    UserAccount() {
//        this.id = "-1";
//        this.companyId = "-1";
//        this.profileId = "-1";
//        this.firstName = new String();
//        this.lastName = new String();
//        this.displayName = new String();
//        this.email = new String();
//        this.deactivated = true;
//        this.location = new String();
//        this.timezone = new String();
//        this.privacy = new UserAccountPrivacy();
//    }
    
//    UserAccount(String id) {
//        this.id = id;
//        this.companyId = "-1";
//        this.profileId = id;
//        this.firstName = new String();
//        this.lastName = new String();
//        this.displayName = new String();
//        this.email = new String();
//        this.deactivated = true;
//        this.location = new String();
//        this.timezone = new String();
//        this.privacy = new UserAccountPrivacy();
//    }
    
    /**
     **/
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    
    /**
     **/
    @JsonProperty("companyId")
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    
    /**
     **/
    @JsonProperty("profileId")
    public String getProfileId() {
        return profileId;
    }
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
    
    
    /**
     **/
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    
    /**
     **/
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
    /**
     **/
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    
    /**
     **/
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    /**
     **/
    @JsonProperty("deactivated")
    public Boolean getDeactivated() {
        return deactivated;
    }
    public void setDeactivated(Boolean deactivated) {
        this.deactivated = deactivated;
    }
    
    
    /**
     **/
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        if(location == null) {
            location = new String();
        }
        this.location = location;
    }
    
    
    /**
     **/
    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    
    /**
     **/
    @JsonProperty("privacy")
    public UserAccountPrivacy getPrivacy() {
        return privacy;
    }
    public void setPrivacy(UserAccountPrivacy privacy) {
        this.privacy = privacy;
    }
    
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) o;
        return Objects.equals(id, userAccount.id) &&
        Objects.equals(companyId, userAccount.companyId) &&
        Objects.equals(profileId, userAccount.profileId) &&
        Objects.equals(firstName, userAccount.firstName) &&
        Objects.equals(lastName, userAccount.lastName) &&
        Objects.equals(displayName, userAccount.displayName) &&
        Objects.equals(email, userAccount.email) &&
        Objects.equals(deactivated, userAccount.deactivated) &&
        Objects.equals(location, userAccount.location) &&
        Objects.equals(timezone, userAccount.timezone) &&
        Objects.equals(privacy, userAccount.privacy);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, companyId, profileId, firstName, lastName, displayName, email, deactivated, location, timezone, privacy);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserAccount {\n");
        
        sb.append("  id: ").append(id).append("\n");
        sb.append("  companyId: ").append(companyId).append("\n");
        sb.append("  profileId: ").append(profileId).append("\n");
        sb.append("  firstName: ").append(firstName).append("\n");
        sb.append("  lastName: ").append(lastName).append("\n");
        sb.append("  displayName: ").append(displayName).append("\n");
        sb.append("  email: ").append(email).append("\n");
        sb.append("  deactivated: ").append(deactivated).append("\n");
        sb.append("  location: ").append(location).append("\n");
        sb.append("  timezone: ").append(timezone).append("\n");
        sb.append("  privacy: ").append(privacy).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
