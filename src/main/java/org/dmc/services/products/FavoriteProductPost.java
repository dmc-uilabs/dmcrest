package org.dmc.services.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class FavoriteProductPost {
    
    private String accountId = null;
    private String serviceId = null;
    
    /**
     **/
    @JsonProperty("accountId")
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     **/
    @JsonProperty("serviceId")
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteProductPost favoriteProductPost = (FavoriteProductPost) o;
        return Objects.equals(serviceId, favoriteProductPost.serviceId)
        && Objects.equals(accountId, favoriteProductPost.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(serviceId, accountId);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FavoriteProductPost {\n");
        
        sb.append("  serviceId: ").append(serviceId).append("\n");
        sb.append("  accountId: ").append(accountId).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
