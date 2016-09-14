package org.dmc.services.users;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dmc.services.data.dao.user.UserOnboardingDao;

public class UserOnboarding {
    private boolean profile;
    private boolean account;
    private boolean company;
    private boolean storefront;
    
    public UserOnboarding() {
        this(false, false, false, false);
    }
                   
    public UserOnboarding(boolean profile, boolean account, boolean company, boolean storefront) {
        this.profile = profile;
        this.account = account;
        this.company = company;
        this.storefront = storefront;
    }
    
    @JsonProperty("profile")
    public boolean getProfile() {
        return profile;
    }
    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    
    @JsonProperty("account")
    public boolean getAccount() {
        return account;
    }
    public void setAccount(boolean account) {
        this.account = account;
    }
    
    @JsonProperty("company")
    public boolean getCompany() {
        return company;
    }
    public void setCompany(boolean company) {
        this.company = company;
    }
    

    @JsonProperty("storefront")
    public boolean getStorefront() {
        return storefront;
    }
    public void setStorefront(boolean storefront) {
        this.storefront = storefront;
    }
    
    public boolean patch(int user_id) {
        UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
        return userOnboardingDao.setUserOnboarding(user_id, this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserOnboarding userOnboarding = (UserOnboarding) o;
        return Objects.equals(profile, userOnboarding.profile) &&
        Objects.equals(account, userOnboarding.account) &&
        Objects.equals(company, userOnboarding.company) &&
        Objects.equals(storefront, userOnboarding.storefront);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(profile, account, company, storefront);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserOnboarding {\n");
        
        sb.append("  profile: ").append(profile).append("\n");
        sb.append("  account: ").append(account).append("\n");
        sb.append("  company: ").append(company).append("\n");
        sb.append("  storefront: ").append(storefront).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
