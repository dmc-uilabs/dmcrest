package org.dmc.services.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.dmc.services.DMCServiceException;

public class FavoriteProductsDao {

	private final String logTag = FavoriteProductsDao.class.getName();
	
    public FavoriteProduct createFavoriteProduct(Integer accountId, Integer serviceId, String userEPPN) throws DMCServiceException {
        return new FavoriteProduct();
    }

    public FavoriteProduct deleteFavoriteProduct(Integer favoriteProductId, String userEPPN) throws DMCServiceException {
        return new FavoriteProduct();
    }
    
    public List<FavoriteProduct> getFavoriteProductForAccounts(List<Integer> accountIds, String userEPPN) throws DMCServiceException {
        return new ArrayList<FavoriteProduct>();
    }

    public List<FavoriteProduct> getFavoriteProductForServices(List<Integer> serviceIds, String userEPPN) throws DMCServiceException {
        return new ArrayList<FavoriteProduct>();
    }

}
