package org.dmc.services.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class FavoriteProduct {
    
    private String id = null;
    private String accountId = null;
    private String serviceId = null;
    
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
        FavoriteProduct favoriteProduct = (FavoriteProduct) o;
        return Objects.equals(id, favoriteProduct.id) && Objects.equals(serviceId, favoriteProduct.serviceId)
        && Objects.equals(accountId, favoriteProduct.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, serviceId, accountId);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetCompareService {\n");
        
        sb.append("  id: ").append(id).append("\n");
        sb.append("  serviceId: ").append(serviceId).append("\n");
        sb.append("  accountId: ").append(accountId).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
