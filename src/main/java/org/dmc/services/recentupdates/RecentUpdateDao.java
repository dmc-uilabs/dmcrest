package org.dmc.services.recentupdates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.Config;
import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.search.SearchException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.User;
import org.dmc.solr.SolrUtils;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

public class RecentUpdateDao {

	private static final String logTag = RecentUpdateDao.class.getName();
	private ResultSet resultSet;

	// Only declare here and instantiate in method where it is used
	// Instantiating here may cause NullPointer Exceptions
	private Connection connection;

    public ArrayList<RecentUpdate> getRecentUpdates(String userEPPN) throws HTTPException {
        ArrayList<RecentUpdate> recentUpdates = new ArrayList<RecentUpdate>();
        ServiceLogger.log(logTag, "User: " + userEPPN + " asking for recent updates");

        try {
            resultSet = DBConnector.executeQuery("SELECT id, update_date, update_type, update_id, parent_id, description FROM recent_update");

            while (resultSet.next()) {

                RecentUpdate recentUpdate = new RecentUpdate();
                recentUpdate.setId(resultSet.getInt("id"));
                recentUpdate.setUpdateDate(resultSet.getString("update_date"));
                recentUpdate.setUpdateType(resultSet.getString("update_type"));
                recentUpdate.setUpdateId(resultSet.getInt("update_id"));
                recentUpdate.setParentId(resultSet.getInt("parent_id"));
                recentUpdate.setDescription(resultSet.getString("description"));

                recentUpdates.add(recentUpdate);
            }

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());
        }
        return recentUpdates;
	}

  public void createRecentUpdate(DMDIIProjectUpdate dmdiiProjectUpdate) throws HTTPException {
    RecentUpdate recentUpdate = new RecentUpdate();

    recentUpdate.setUpdateDate(dmdiiProjectUpdate.getDate().toString());
    recentUpdate.setUpdateType("update");
    recentUpdate.setUpdateId(dmdiiProjectUpdate.getId());
    recentUpdate.setParentId(dmdiiProjectUpdate.getProject().getId());
    recentUpdate.setDescription(dmdiiProjectUpdate.getTitle());

  }

}
