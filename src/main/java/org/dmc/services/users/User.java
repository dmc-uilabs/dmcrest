package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */

import java.util.ArrayList;
import org.dmc.services.ServiceLogger;


public class User {
    private final String logTag = User.class.getName();
    
    private int id; // out of spec
    private final String userName; // out of spec
    private final String realName; // out of spec
    
    private final String displayName;
    private final int accountId;
    private final int profileId;
    private final int companyId;
    private final int role;
    private final boolean termsConditions;
    private UserNotifications notifications;
    private UserRunningServices runningServices;
    private UserMessages messages;
    private final UserOnboarding onboarding;
    
    public User() {
        this.id = -1;
        this.userName = null;
        this.realName = null;
        
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
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        
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
        
        this.id = userBuilder.id;
        this.userName = userBuilder.userName;
        this.realName = userBuilder.realName;
        
        this.displayName = userBuilder.realName;
        this.accountId = userBuilder.id;
        this.profileId = -1;
        this.companyId = -1;
        this.role = -1;
        this.termsConditions = false;
        this.notifications = new UserNotifications();
        this.runningServices = new UserRunningServices();
        this.messages = new UserMessages();
        this.onboarding = UserOnboardingDao.getUserOnboarding(userBuilder.id);
    }

    public int getId() {  // out of spec
        return id;
    }

    public String getUserName() { // out of spec
        return userName;
    }

    public String getRealName() { // out of spec
        return realName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public int getAccountId() {
        return accountId;
    }
    
    public int getProfileId() {
        return profileId;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    public int getRole() {
        return role;
    }
    
    public boolean getTermsConditions() {
        return termsConditions;
    }
    
    public UserNotifications getNotifications() {
        return notifications;
    }
    
    public UserRunningServices getRunningServices() {
        return runningServices;
    }
    
    public UserMessages getMessages() {
        return messages;
    }
    
    public UserOnboarding getOnboarding() {
        return onboarding;
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

        public void userName (String userName) {
            this.userName = userName;
        }

        public void realName (String realName) {
            this.realName = realName;
        }

        public User build() {
            return new User(this);
        }
    }


}

