package org.dmc.services.userfavorite;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.entities.FavoriteContentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoriteContentTypeDao {

    private final String logTag = FavoriteContentTypeDao.class.getName();
    private Connection connection = null;

    public FavoriteContentType getFavoriteContentType(int contentTypeId) {

        FavoriteContentType favoriteContentType = new FavoriteContentType();

        try {

            connection = DBConnector.connection();
            connection.setAutoCommit(false);

            PreparedStatement getFavoriteContentType = null;
            String query = "SELECT id, type FROM fav_content_type WHERE id = ?";
            getFavoriteContentType = connection.prepareStatement(query);
            getFavoriteContentType.setInt(1, contentTypeId);
            final ResultSet resultSet = getFavoriteContentType.executeQuery();
            connection.commit();

            while (resultSet.next()) {
                favoriteContentType = readFavoriteContentType(resultSet);
            }

            return favoriteContentType;

        } catch (SQLException e ) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get favorite content type with "
                + " the id " + contentTypeId);
        }

    }

    private FavoriteContentType readFavoriteContentType(ResultSet resultSet) throws SQLException {
        final FavoriteContentType favoriteContentType = new FavoriteContentType();

        favoriteContentType.setId(resultSet.getInt("id"));
        favoriteContentType.setContentType(resultSet.getString("type"));

        return favoriteContentType;
    }

}
