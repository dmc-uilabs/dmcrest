package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */
import java.util.Objects;

import java.util.ArrayList;
import org.dmc.services.ServiceLogger;

import com.fasterxml.jackson.annotation.JsonProperty;

// This class' name should be changed to UserDetails to match yaml
public class User {
    private final String logTag = User.class.getName();
    
    private String displayName;
    private int accountId;
    private int profileId;
    private int companyId;
    private int role;
    private boolean termsConditions;
    private UserNotifications notifications;
    private UserRunningServices runningServices;
    private UserMessages messages;
    private UserOnboarding onboarding;
    
    public User() {
        this.displayName = null; //TODO: fix
        this.accountId = -1;
        this.profileId = -1;
        this.companyId = -1;
        this.role = -1;
        this.termsConditions = false;
        this.notifications = new UserNotifications();
        this.runningServices = new UserRunningServices();
        this.messages = new UserMessages();
        this.onboarding = new UserOnboarding(false, false, false, false);
        
    }
    
    public User (int id, String userName, String realName, boolean termsConditions) {
        ServiceLogger.log(logTag, "In User with id " + id + " userName " + userName);
        this.displayName = realName;
        this.accountId = id;
        this.profileId = id;
        this.companyId = -1;
        this.role = -1;
        this.termsConditions = termsConditions;
        this.notifications = new UserNotifications();
        this.runningServices = new UserRunningServices();
        this.messages = new UserMessages();
        this.onboarding = UserOnboardingDao.getUserOnboarding(id);
    }

    public User (UserBuilder userBuilder) {
        this.displayName = userBuilder.realName;
        this.accountId = userBuilder.id;
        this.profileId = -1;
        this.companyId = userBuilder.companyId;
        this.role = -1;
        this.termsConditions = false;
        this.notifications = new UserNotifications();
        this.runningServices = new UserRunningServices();
        this.messages = new UserMessages();
        this.onboarding = UserOnboardingDao.getUserOnboarding(userBuilder.id);
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
    @JsonProperty("accountId")
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = Integer.parseInt(accountId);
    }
    
    
    /**
     **/
    @JsonProperty("profileId")
    public int getProfileId() {
        return profileId;
    }
    public void setProfileId(String profileId) {
        this.profileId = Integer.parseInt(profileId);
    }

    
    /**
     **/
    @JsonProperty("companyId")
    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = Integer.parseInt(companyId);
    }
    
    
    /**
     **/
    @JsonProperty("role")
    public int getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = Integer.parseInt(role);
    }
    
    
    /**
     **/
    @JsonProperty("termsConditions")
    public boolean getTermsConditions() {
        return termsConditions;
    }
    public void setTermsConditions(Boolean termsConditions) {
        this.termsConditions = termsConditions;
    }
    
    
    /**
     **/
    @JsonProperty("notifications")
    public UserNotifications getNotifications() {
        return notifications;
    }
    public void setNotifications(UserNotifications notifications) {
        this.notifications = notifications;
    }
    
    
    /**
     **/
    @JsonProperty("runningServices")
    public UserRunningServices getRunningServices() {
        return runningServices;
    }
    public void setRunningServices(UserRunningServices runningServices) {
        this.runningServices = runningServices;
    }
    
    
    /**
     **/
    @JsonProperty("messages")
    public UserMessages getMessages() {
        return messages;
    }
    public void setMessages(UserMessages messages) {
        this.messages = messages;
    }
    
    
    /**
     **/
    @JsonProperty("onboarding")
    public UserOnboarding getOnboarding() {
        return onboarding;
    }
    public void setOnboarding(UserOnboarding onboarding) {
        this.onboarding = onboarding;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User userDetails = (User) o;
        return Objects.equals(displayName, userDetails.displayName) &&
        Objects.equals(accountId, userDetails.accountId) &&
        Objects.equals(profileId, userDetails.profileId) &&
        Objects.equals(companyId, userDetails.companyId) &&
        Objects.equals(role, userDetails.role) &&
        Objects.equals(termsConditions, userDetails.termsConditions) &&
        Objects.equals(notifications, userDetails.notifications) &&
        Objects.equals(runningServices, userDetails.runningServices) &&
        Objects.equals(messages, userDetails.messages) &&
        Objects.equals(onboarding, userDetails.onboarding);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(displayName, accountId, profileId, companyId, role, termsConditions, notifications, runningServices, messages, onboarding);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserAccount {\n");
        
        sb.append("  displayName: ").append(displayName).append("\n");
        sb.append("  accountId: ").append(accountId).append("\n");
        sb.append("  profileId: ").append(profileId).append("\n");
        sb.append("  companyId: ").append(companyId).append("\n");
        sb.append("  role: ").append(role).append("\n");
        sb.append("  termsConditions: ").append(termsConditions).append("\n");
        sb.append("  notifications: ").append(notifications).append("\n");
        sb.append("  runningServices: ").append(runningServices).append("\n");
        sb.append("  messages: ").append(messages).append("\n");
        sb.append("  onboarding: ").append(onboarding).append("\n");
        sb.append("}\n");
        return sb.toString();
    }


    public static class UserBuilder {

        private int id;
        private String userName;
        private String realName;
        private int companyId;

        public UserBuilder (int id) {
            this.id = id;
        }

        public UserBuilder (int id, String userName) {
            this.id = id;
            this.userName = userName;
        }

        public UserBuilder (int id, String userName, String realName) {
            this.id = id;
            this.realName = realName;
            this.userName = userName;
        }

        public UserBuilder (int id, String userName, String realName, int companyId) {
            this.id = id;
            this.realName = realName;
            this.userName = userName;
            this.companyId = companyId;
        }

        public void userName (String userName) {
            this.userName = userName;
        }

        public void realName (String realName) {
            this.realName = realName;
        }

        public User build() {
            return new User(this);
        }

        public void companyId (int companyId) { this.companyId = companyId; }

    }


}

