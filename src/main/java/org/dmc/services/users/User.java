package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */

import java.util.ArrayList;

public class User {

    public class Onboarding {
        private final boolean profile;
        private final boolean account;
        private final boolean company;
        private final boolean storefront;
        
        Onboarding(boolean profile, boolean account, boolean company, boolean storefront) {
            this.profile = profile;
            this.account = account;
            this.company = company;
            this.storefront = storefront;
        }
        
        public boolean getProfile() {
            return profile;
        }

        public boolean getAccount() {
            return account;
        }

        public boolean getCompany() {
            return company;
        }

        public boolean getStorefront() {
            return storefront;
        }
    }
    
    private int id;
    private final String userName;
    private final String realName;
    
    private final String displayName;
    private final int accountId;
    private final int profileId;
    private final int companyId;
    private final int role;
    private final boolean termsConditions;
    private UserNotifications notifications;
    private UserServices services;
    private UserMessages messages;
    private final Onboarding onboarding;
    
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
        this.services = new UserServices();
        this.messages = new UserMessages();
        this.onboarding = new Onboarding(false, false, false, false);
        
    }
    
    public User (int id, String userName, String realName) {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        
        this.displayName = null; //TODO: fix
        this.accountId = -1;
        this.profileId = -1;
        this.companyId = -1;
        this.role = -1;
        this.termsConditions = false;
        this.notifications = null;
        this.services = null;
        this.messages = null;
        this.onboarding = new Onboarding(false, false, false, false);
    }

    public User (UserBuilder userBuilder) {
        
        this.id = userBuilder.id;
        this.userName = userBuilder.userName;
        this.realName = userBuilder.realName;
        
        this.displayName = null; //TODO: fix
        this.accountId = -1;
        this.profileId = -1;
        this.companyId = -1;
        this.role = -1;
        this.termsConditions = false;
        this.notifications = null;
        this.services = null;
        this.messages = null;
        this.onboarding = new Onboarding(false, false, false, false);
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
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
    
    public UserServices getServices() {
        return services;
    }
    
    public UserMessages getMessages() {
        return messages;
    }
    
    public Onboarding getOnboarding() {
        return onboarding;
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

