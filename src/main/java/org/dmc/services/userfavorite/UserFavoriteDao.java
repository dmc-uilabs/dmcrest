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
                + userId + ": " + e.getMessage());
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
