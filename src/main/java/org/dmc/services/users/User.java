package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */

import java.util.ArrayList;
import org.dmc.services.notification.Notification;
import org.dmc.services.services.Service;
import org.dmc.services.message.Message;

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
    private ArrayList<Notification> notifications;
    private ArrayList<Service> services;
    private ArrayList<Message> messages;
    private final Onboarding onboarding;
    
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

