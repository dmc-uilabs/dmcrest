package org.dmc.services.userfavorite;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.entities.UserFavorite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserFavoriteDao {

    private final String logTag = UserFavoriteDao.class.getName();
    private Connection connection = null;
    private FavoriteContentTypeDao favoriteContentTypeDao = new FavoriteContentTypeDao();

    public UserFavorite getUserFavorite (Integer id) {

        try {

            connection = DBConnector.connection();
            connection.setAutoCommit(false);

            PreparedStatement getUserFavorite = null;

            UserFavorite userFavorite = null;

            String query = "SELECT user_id, content_id, content_type  FROM user_favorite WHERE id = ?";

            getUserFavorite = connection.prepareStatement(query);
            getUserFavorite.setInt(1, id);
            final ResultSet resultSet = getUserFavorite.executeQuery();
            connection.commit();

            while (resultSet.next()) {
                userFavorite = readUserFavoriteResultSet(resultSet);
            }

            return userFavorite;

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get user favorite");
        }

    }

    public ArrayList<UserFavorite> getUserFavorites(Integer userId, Integer contentType) throws DMCServiceException {

        ArrayList<UserFavorite> userFavorites = new ArrayList<>();

        try {

            connection = DBConnector.connection();
            connection.setAutoCommit(false);

            PreparedStatement getUserFavorites = null;
            String query = "SELECT user_id, content_id, content_type  FROM user_favorite WHERE user_id = ?";
            if (contentType != null) {
                query += " AND content_type = ?";
            }
            getUserFavorites = connection.prepareStatement(query);
            getUserFavorites.setInt(1, userId);
            if (contentType != null) {
                getUserFavorites.setInt(2, contentType);
            }
            final ResultSet resultSet = getUserFavorites.executeQuery();
            connection.commit();

            while (resultSet.next()) {
                UserFavorite userFavorite = readUserFavoriteResultSet(resultSet);
                userFavorites.add(userFavorite);
            }

            return userFavorites;

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get user favorites for user "
                + userId);
        }

    }

    public UserFavorite save(UserFavorite userFavorite) throws DMCServiceException {
        try {
            connection = DBConnector.connection();
            connection.setAutoCommit(false);

            String query = "INSERT INTO user_favorite (user_id, content_id, content_type) "
                    + " values (?, ?, ?)";

            final PreparedStatement saveUserFavorite = connection.prepareStatement(query);

            saveUserFavorite.setInt(1, userFavorite.getUserId());
            saveUserFavorite.setInt(2, userFavorite.getContentId());
            saveUserFavorite.setInt(3, userFavorite.getContentType().getId());

            Integer newUserFavoriteId = saveUserFavorite.executeUpdate();
            connection.commit();

            return getUserFavorite(newUserFavoriteId);

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (null != connection) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + ex.getMessage());
                }
            }
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to save user favorites object");
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (Exception ex) {
                   ServiceLogger.log(logTag, ex.getMessage());
                }
            }
        }
    }

    public Integer delete(UserFavorite userFavorite) throws DMCServiceException {
        try {

            connection = DBConnector.connection();
            connection.setAutoCommit(false);

            String query = "DELETE FROM user_favorite WHERE user_id = ? AND content_id = ? AND content_type = ?";

            final PreparedStatement deleteUserFavorite = connection.prepareStatement(query);

            deleteUserFavorite.setInt(1, userFavorite.getUserId());
            deleteUserFavorite.setInt(2, userFavorite.getContentId());
            deleteUserFavorite.setInt(3, userFavorite.getContentType().getId());

            Integer returnValue = deleteUserFavorite.executeUpdate();
            connection.commit();

            return returnValue;

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (null != connection) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + ex.getMessage());
                }
            }
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to delete user favorite");
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (Exception ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
            }
        }
    }

    private UserFavorite readUserFavoriteResultSet(ResultSet resultSet) throws SQLException {
        final UserFavorite userFavorite = new UserFavorite();

        userFavorite.setContentId(resultSet.getInt("content_id"));
        userFavorite.setUserId(resultSet.getInt("user_id"));
        userFavorite.setContentType(favoriteContentTypeDao.getFavoriteContentType(resultSet.getInt("content_type")));

        return userFavorite;
    }
}
