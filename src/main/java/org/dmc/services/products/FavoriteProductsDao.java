package org.dmc.services.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.sharedattributes.Util;


public class FavoriteProductsDao {

	private final String logTag = FavoriteProductsDao.class.getName();
	
    public FavoriteProduct createFavoriteProduct(Integer accountId, Integer serviceId, String userEPPN) throws DMCServiceException {
        int id = -1;
        int userId = -1;
        Util util = Util.getInstance();

        try {
            userId = CompanyUserUtil.getUserId(userEPPN);
        } catch(SQLException sqlEx) {
            throw new DMCServiceException(DMCError.InvalidAccountId, sqlEx.toString());
        }
        
        if(accountId.intValue() != userId) {
            throw new DMCServiceException(DMCError.OtherSQLError, "User " + userEPPN + " id does not match the accountId " + accountId);
        }
        
        String sqlInsertFavoriteProduct = "INSERT INTO favorite_products(account_id, service_id) VALUES (?,?)";
        
        try {
            PreparedStatement preparedStatement = null;
            // Insert into organization_review_reply
            preparedStatement = DBConnector.prepareStatement(sqlInsertFavoriteProduct, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, serviceId);
            
            int rCreate = preparedStatement.executeUpdate();
        
            id = util.getGeneratedKey(preparedStatement, "id");
            ServiceLogger.log(logTag, "Created Favorite Product: " + id);
            
        } catch (SQLException sqlEx) {
            throw new DMCServiceException(DMCError.OtherSQLError, sqlEx.toString());
        }
        
        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setId(Integer.toString(id));
        favoriteProduct.setAccountId(accountId.toString());
        favoriteProduct.setServiceId(serviceId.toString());
        
        return favoriteProduct;
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
