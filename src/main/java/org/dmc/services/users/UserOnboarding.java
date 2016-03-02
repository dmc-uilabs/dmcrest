package org.dmc.services.users;

public class UserOnboarding {
    private final boolean profile;
    private final boolean account;
    private final boolean company;
    private final boolean storefront;
    
    UserOnboarding() {
        this(false, false, false, false);
    }
                   
    UserOnboarding(boolean profile, boolean account, boolean company, boolean storefront) {
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
