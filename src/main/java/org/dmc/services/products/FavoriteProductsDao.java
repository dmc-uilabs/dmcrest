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
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.sharedattributes.Util;


public class FavoriteProductsDao {

	private final String logTag = FavoriteProductsDao.class.getName();
	
    public FavoriteProduct createFavoriteProduct(Integer accountId, Integer serviceId, String userEPPN) throws DMCServiceException {
        Util util = Util.getInstance();
        int id = -1;
        int userId = getUserId(userEPPN);
        
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

    public void deleteFavoriteProduct(Integer favoriteProductId, String userEPPN) throws DMCServiceException {
        ServiceLogger.log(logTag, "In deleteFavoriteProduct " + favoriteProductId + " for userEPPN: " + userEPPN);
        Util util = Util.getInstance();
        
        int userId = getUserId(userEPPN);
        
        String deleteUserAccountServerQuery = "DELETE FROM favorite_products WHERE account_id = ? AND id = ?";
        
        PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteUserAccountServerQuery);
        try {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, favoriteProductId);
            int change = preparedStatement.executeUpdate();
            if(change != 1) {
                throw new DMCServiceException(DMCError.OtherSQLError, "number of changes were: " + change); // single item not deleted
            }
            
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }
        return;
    }
    
    public List<FavoriteProduct> getFavoriteProductForAccounts(List<Integer> accountIds, String userEPPN) throws DMCServiceException {
        return new ArrayList<FavoriteProduct>();
    }

    public List<FavoriteProduct> getFavoriteProductForServices(List<Integer> serviceIds, String userEPPN) throws DMCServiceException {
        return new ArrayList<FavoriteProduct>();
    }

    int getUserId(String userEPPN){
        try {
            return UserDao.getUserID(userEPPN);
        } catch(SQLException sqlEx) {
            throw new DMCServiceException(DMCError.UnknownUser, sqlEx.toString());
        }
    }
}
