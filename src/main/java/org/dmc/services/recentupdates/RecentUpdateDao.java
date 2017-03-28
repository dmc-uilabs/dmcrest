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
import java.lang.reflect.Method;
import java.lang.Class;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.Config;
import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.entities.DMDIIDocument;
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
import org.joda.time.DateTime;
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

  // public void createRecentUpdate(DMDIIProjectUpdate dmdiiProjectUpdate) throws HTTPException {
	// 	// Set variables for the attributes of a project updat
	// 	java.sql.Timestamp date = new java.sql.Timestamp(dmdiiProjectUpdate.getDate().getTime());
	// 	String updateType = "update";
	// 	int updateId = dmdiiProjectUpdate.getId();
	// 	int parentId = dmdiiProjectUpdate.getProject().getId();
	// 	String description = dmdiiProjectUpdate.getTitle();
	//
	// 	// Run SQL insert of those values
	// 	try {
	// 		String query = "INSERT INTO recent_update (update_date, update_type, update_id, parent_id, description)"
	// 		+ "values ( ?, ?, ?, ?, ?)";
	//
	// 		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
	// 		preparedStatement.setTimestamp(1, date);
	// 		preparedStatement.setString(2, updateType);
	// 		preparedStatement.setInt(3, updateId);
	// 		preparedStatement.setInt(4, parentId);
	// 		preparedStatement.setString(5, description);
	// 		preparedStatement.executeUpdate();
	//
	// 	} catch (SQLException e) {
	// 			ServiceLogger.log(logTag, e.getMessage());
	// 	}
	//
  // }
	//
	// public void createRecentUpdate(DMDIIDocument dmdiiDocument) throws HTTPException {
	// 	// Set variables for the attributes of a project updat
	// 	// java.sql.Timestamp date = new java.sql.Timestamp(dmdiiDocument.getModified().getTime());
	// 	String updateType = "dmdiiDocument";
	// 	int updateId = dmdiiDocument.getId();
	// 	int parentId = dmdiiDocument.getDmdiiProject().getId();
	// 	String description = dmdiiDocument.getDocumentName();
	//
	// 	try {
	// 		insertUpdate(updateType, updateId, parentId, description);
	// 	} catch (SQLException e) {
	// 		ServiceLogger.log(logTag, e.getMessage());
	// 	}
	//
	// 	// Run SQL insert of those values
	// 	// try {
	// 	// 	String query = "INSERT INTO recent_update (update_date, update_type, update_id, parent_id, description)"
	// 	// 	+ "values ( ?, ?, ?, ?, ?)";
	// 	//
	// 	// 	PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
	// 	// 	preparedStatement.setTimestamp(1, date);
	// 	// 	preparedStatement.setString(2, updateType);
	// 	// 	preparedStatement.setInt(3, updateId);
	// 	// 	preparedStatement.setInt(4, parentId);
	// 	// 	preparedStatement.setString(5, description);
	// 	// 	preparedStatement.executeUpdate();
	// 	//
	// 	// } catch (SQLException e) {
	// 	// 		ServiceLogger.log(logTag, e.getMessage());
	// 	// }
	//
  // }

	// This function is for newly-created updates, and therefore does no comparison
	//	with the 'original' object
	public void createRecentUpdate(Object updatedItem) throws HTTPException {

		try {
			switch (updatedItem.getClass().getSimpleName()) {
				case "DMDIIDocument":  addDMDIIDocumentUpdate((DMDIIDocument)updatedItem);
					break;
				case "DMDIIProjectUpdate": addDMDIIProjectUpdate((DMDIIProjectUpdate)updatedItem);
				default: break;
			}
		} catch (SQLException e) {

		}

  }

	// This function is for updates on existing entries, and therefore does do a
	//	comparison with the 'original' object
	public void createRecentUpdate(Object updatedItem, Object originalItem) throws HTTPException {
		// switch (itemClass.getSimpleName()) {
		// 	case "DMDIIDocument":  addDMDIIDocumentUpdate((DMDIIDocument)updatedItem);
		// 		break;
		// 	case "DMDIIProjectUpdate": addDMDIIProjectUpdate((DMDIIProjectUpdate)updatedItem);
		// 	default: break;
		// }

		Class itemClass = originalItem.getClass();

		// try {

			Method[] methods = itemClass.getMethods();
			for(int i = 0; i < methods.length; i++) {
				String methName = methods[i].getName();
				System.out.println(methName);

				if (methName.startsWith("get")) {

					// String origValue = methods[i].invoke(originalItem).toString();
					String origValue = valueToString(methods[i], originalItem);
					String newValue = valueToString(methods[i], updatedItem);

					System.out.println(origValue);
					System.out.println(newValue);

					if (origValue.equals(newValue)) {
						System.out.println("!!!!values match!!!!");
						// System.out.println(origValue);
						// System.out.println(newValue);
						// String attributeName = methName.replace("get","");
						// String description = attributeName+" has been updated";
						// String internalDescription = "From "++" to "+;
					}
				}
			}

			// TODO: add exception handling
		// } catch (java.lang.reflect.InvocationTargetException e) {}

	}

	private String valueToString(Method method, Object item) {

		Class itemClass = item.getClass();
		Class baseEntity;

		try {
			baseEntity = Class.forName("BaseEntity");
		} catch (java.lang.ClassNotFoundException e) {}

		System.out.println("is assignable");
		System.out.println(baseEntity.isAssignableFrom(itemClass));

		try {
			return method.invoke(item).toString();
		} catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return "";
		}
	}

	private void addDMDIIDocumentUpdate(DMDIIDocument dmdiiDocument) throws SQLException {
		String description = dmdiiDocument.getDocumentName();
		addDMDIIDocumentUpdate(dmdiiDocument, description);
	}

	private void addDMDIIDocumentUpdate(DMDIIDocument dmdiiDocument, String description) throws SQLException {
		String updateType = dmdiiDocument.getClass().getSimpleName();
		int updateId = dmdiiDocument.getId();
		int parentId = dmdiiDocument.getDmdiiProject().getId();
		int userId = dmdiiDocument.getOwner().getId();

		try {
			insertUpdate(updateType, updateId, parentId, description, userId);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	private void addDMDIIProjectUpdate(DMDIIProjectUpdate dmdiiProjectUpdate) throws SQLException {

			String description = dmdiiProjectUpdate.getTitle();
			addDMDIIProjectUpdate(dmdiiProjectUpdate, description);

	}

	private void addDMDIIProjectUpdate(DMDIIProjectUpdate dmdiiProjectUpdate, String description) throws SQLException {
			String updateType = dmdiiProjectUpdate.getClass().getSimpleName();
			int updateId = dmdiiProjectUpdate.getId();
			int parentId = dmdiiProjectUpdate.getProject().getId();
			int userId = dmdiiProjectUpdate.getCreator().getId();

		try {
			insertUpdate(updateType, updateId, parentId, description, userId);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	private void insertUpdate(String updateType, int updateId, int parentId, String description, int userId) throws SQLException {

		Long currentTime = System.currentTimeMillis() / 1000L;
		System.out.println(currentTime);

		try {
			String query = "INSERT INTO recent_update (update_date, update_type, update_id, parent_id, description, user_id)"
			+ "values ( ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			// preparedStatement.setTimestamp(1, date);
			preparedStatement.setLong(1, currentTime);
			preparedStatement.setString(2, updateType);
			preparedStatement.setInt(3, updateId);
			preparedStatement.setInt(4, parentId);
			preparedStatement.setString(5, description);
			preparedStatement.setInt(6, userId);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
		}
	}

}
