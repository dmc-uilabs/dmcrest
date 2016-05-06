package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectDao;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.users.UserDao;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceDao {

	private final String logTag = ServiceDao.class.getName();
    private Connection connection;
	private ResultSet resultSet;

	public Service getService(int requestId) {
		int id = 0;
		String serviceType = "", title = "", description = "", startDate = "", startTime = "";
		ArrayList<String> tags = new ArrayList<String>();
		
		Service service = new Service();
		
		try {
			
			String query = "SELECT * FROM service WHERE service_id = " + requestId;

			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next()) {
				
				service.setId(requestId);
				service.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
				service.setTitle(resultSet.getString("title"));
				service.setDescription(resultSet.getString("description"));
				service.setOwner(resultSet.getString("owner_id"));
				service.setProfileId(resultSet.getString("owner_id"));  // ToDo: up date
				service.setReleaseDate(resultSet.getDate("release_date"));
				service.setType(resultSet.getString("service_type"));
				service.setTags(new ArrayList<String>()); // ToDo: up date
				service.setSpecifications(resultSet.getString("specifications"));

				service.setFeatureImage(new FeatureImage("", ""));
				service.setCurrentStatus(new ServiceCurrentStatus(0, "", ""));
				
				service.setProjectId(resultSet.getString("project_id"));
				service.setFrom(resultSet.getString("from_location"));
				service.setType(resultSet.getString("type"));
				service.setParent(resultSet.getString("parent"));
				service.setPublished(resultSet.getBoolean("published"));

				service.setAverageRun("");
								
			}
			return service;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

    public Service createService(Service requestedBody, String userEPPN) throws DMCServiceException {
        Connection connection = null;
        try {
            connection = DBConnector.connection();
            // let's start a transaction
            connection.setAutoCommit(false);

            // look up userID
            int userID = UserDao.getUserID(userEPPN);

            String query = "insert into service (organization_id, title, description, owner_id, release_date, service_type, project_id, from_location, type, parent, published)";
            query += "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setObject(1, requestedBody.getCompanyIdAsInteger(), java.sql.Types.INTEGER);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3,  requestedBody.getDescription());
            preparedStatement.setInt(4,  userID);
            preparedStatement.setObject(5, requestedBody.getReleaseDateAsSqlDate(), java.sql.Types.DATE);
            preparedStatement.setString(6, requestedBody.getServiceType());
            preparedStatement.setObject(7, requestedBody.getProjectIdAsInteger(), java.sql.Types.INTEGER);
            preparedStatement.setString(8, requestedBody.getFrom());
            preparedStatement.setString(9, requestedBody.getType());
            preparedStatement.setString(10, requestedBody.getParent());
            preparedStatement.executeUpdate();

            query = "select currval('service_service_id_seq') as id";
            resultSet = DBConnector.executeQuery(query);

            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            Service service = getService(id);
            connection.commit();
            return service;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (null != connection) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + ex.getMessage());
                }
            }
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to create new service: " + e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }
}
