package org.dmc.services.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.utils.SQLUtils;


public class FavoriteProductsDao {

    private final Integer DEFAULT_LIMIT = 25;
    private final String DEFAULT_ORDER = "DESC";
    private final Integer DEFAULT_START = 0;
    private final String DEFAULT_SORT = "id";
    
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
            // if it is a duplicate we'll continue without error,
            // other errors will fail
            if (!sqlEx.getMessage().contains("duplicate key")) {
                throw new DMCServiceException(DMCError.OtherSQLError, sqlEx.toString());
            }
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
        
        String sqlDeleteFavoriteProduct = "DELETE FROM favorite_products WHERE account_id = ? AND id = ?";
        
        PreparedStatement preparedStatement = DBConnector.prepareStatement(sqlDeleteFavoriteProduct);
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
        return getFavoriteProductForAccounts(accountIds, DEFAULT_LIMIT, DEFAULT_ORDER, DEFAULT_START, DEFAULT_SORT, userEPPN);
    }
    
    public List<FavoriteProduct> getFavoriteProductForAccounts(List<Integer> accountIds, Integer limit, String order, Integer start, String sort, String userEPPN) throws DMCServiceException {
        return getFavoriteProductByIds(accountIds, limit, order, start, sort, userEPPN, "account_id");
    }
    
    public List<FavoriteProduct> getFavoriteProductForServices(List<Integer> serviceIds, String userEPPN) throws DMCServiceException {
        return getFavoriteProductForServices(serviceIds, DEFAULT_LIMIT, DEFAULT_ORDER, DEFAULT_START, DEFAULT_SORT, userEPPN);
    }
    
    private List<FavoriteProduct> getFavoriteProductForServices(List<Integer> serviceIds, Integer limit, String order, Integer start, String sort, String userEPPN) throws DMCServiceException {
        return getFavoriteProductByIds(serviceIds, limit, order, start, sort, userEPPN, "service_id");
    }

    
    private List<FavoriteProduct> getFavoriteProductByIds(List<Integer> ids, Integer limit, String order, Integer start, String sort, String userEPPN, String column) {
        ListIterator<Integer> iterator = ids.listIterator();
        ArrayList<FavoriteProduct> favoriteProducts = new ArrayList<FavoriteProduct>();

        
        //int userId = getUserId(userEPPN);
        // need to check is user_id can see accountId's favorite products
        
        while(iterator.hasNext()) {
            Integer id = iterator.next();
/*
            ArrayList<String> validFieldsForSort = new ArrayList<String>();
            validFieldsForSort.add("id");
            final String orderClause = SQLUtils.buildOrderByClause(order, sort, validFieldsForSort);
  */
//            String sqlSelectFavoriteProduct = "SELECT * FROM favorite_products WHERE " + column + " = ? "+orderClause+" LIMIT ? OFFSET ?";
            String sqlSelectFavoriteProduct = "SELECT favorite_products.* FROM favorite_products INNER JOIN service ON (favorite_products.service_id = service.service_id AND service.project_id != 0) WHERE favorite_products." + column + " = ? LIMIT ? OFFSET ?";

            ServiceLogger.log(logTag, sqlSelectFavoriteProduct);
            
            PreparedStatement preparedStatement = DBConnector.prepareStatement(sqlSelectFavoriteProduct);
            
            try {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, limit);
                preparedStatement.setInt(3, start);

                final ResultSet resultSet = preparedStatement.executeQuery();
            
                while(resultSet.next()) {
                    FavoriteProduct favoriteProduct = new FavoriteProduct();
                    favoriteProduct.setId(Integer.toString(resultSet.getInt("id")));
                    favoriteProduct.setAccountId(Integer.toString(resultSet.getInt("account_id")));
                    favoriteProduct.setServiceId(Integer.toString(resultSet.getInt("service_id")));
                    favoriteProducts.add(favoriteProduct);
                }
            } catch (SQLException e) {
                ServiceLogger.log(logTag, e.getMessage());
                throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
            }
        }
        return favoriteProducts;
    }
    
    public List<Service> getMostPopularProducts(Integer limit, String order, Integer start, String sort, String userEPPN) {
        List<Service> services = new ArrayList<Service>();
        ServiceDao serviceDao = new ServiceDao();
        
/*        ArrayList<String> validFieldsForSort = new ArrayList<String>();
        validFieldsForSort.add("count");
        validFieldsForSort.add("id");
        final String orderClause = SQLUtils.buildOrderByClause(order, sort, validFieldsForSort);
  */
        String sqlSelectFavoriteProduct = "select favorite_products.service_id, count(favorite_products.service_id) AS thecount FROM favorite_products INNER JOIN service ON (favorite_products.service_id = service.service_id AND service.project_id != 0) GROUP BY favorite_products.service_id ORDER BY thecount LIMIT ? OFFSET ?";
        ServiceLogger.log(logTag, sqlSelectFavoriteProduct);

        PreparedStatement preparedStatement = DBConnector.prepareStatement(sqlSelectFavoriteProduct);
        try {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, start);
            
            final ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                services.add(serviceDao.getService(resultSet.getInt("service_id"), userEPPN));
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }

        return services;
    }
    
    int getUserId(String userEPPN){
        try {
            return UserDao.getUserID(userEPPN);
        } catch(SQLException sqlEx) {
            throw new DMCServiceException(DMCError.UnknownUser, sqlEx.toString());
        }
    }
}
