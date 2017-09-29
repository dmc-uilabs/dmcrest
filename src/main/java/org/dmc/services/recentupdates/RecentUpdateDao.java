package org.dmc.services.recentupdates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.Config;
import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.dmc.services.data.entities.Organization;
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
	private Connection connection;

    public ArrayList<RecentUpdate> getRecentUpdates(String userEPPN, int limit) throws HTTPException {
        ArrayList<RecentUpdate> recentUpdates = new ArrayList<RecentUpdate>();
        ServiceLogger.log(logTag, "User: " + userEPPN + " asking for recent updates");

        try {
						resultSet = DBConnector.executeQuery("select ru.id, update_date, update_type, update_id, ru.parent_id, ru.description, project_title, org.name, attribute_name from recent_update ru left join dmdii_project dp on dp.id = ru.parent_id and ru.update_type in ('DMDIIDocument', 'DMDIIProjectUpdate','DMDIIProject') and dp.is_deleted = 'f' left join dmdii_project_update pu on pu.id = ru.update_id  and ru.update_type = 'DMDIIProjectUpdate' and pu.is_deleted = 'f' left join dmdii_document dd on dd.id = ru.update_id and ru.update_type = 'DMDIIDocument' and dd.is_deleted = 'f' left join organization org on org.organization_id = ru.parent_id and ru.update_type in ('DMDIIMember') WHERE (parent_id = update_id OR pu.id is not NULL OR dd.id is not NULL OR update_type='DMDIIMember') AND ((attribute_name = '' AND update_type in ('DMDIIDocument','DMDIIProjectUpdate')) OR (update_type='DMDIIProject' and attribute_name in ('ProjectStatus','')) OR (update_type='DMDIIMember')) order by ru.id desc limit "+Integer.toString(limit));

            while (resultSet.next()) {

                RecentUpdate recentUpdate = new RecentUpdate();
                recentUpdate.setId(resultSet.getInt("id"));
                recentUpdate.setUpdateDate(resultSet.getString("update_date"));
                recentUpdate.setUpdateType(resultSet.getString("update_type"));
                recentUpdate.setUpdateId(resultSet.getInt("update_id"));
                recentUpdate.setParentId(resultSet.getInt("parent_id"));
                recentUpdate.setDescription(resultSet.getString("description"));
								String parentTitle = resultSet.getString("project_title") != null ? resultSet.getString("project_title") : resultSet.getString("name");
								recentUpdate.setParentTitle(parentTitle);
								recentUpdate.setAttributeName(resultSet.getString("attribute_name"));

                recentUpdates.add(recentUpdate);
            }

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());
        }
        return recentUpdates;
	}

	// This function is for newly-created updates, and therefore does no comparison
	//	with the 'original' object
	public void createRecentUpdate(Object updatedItem) throws HTTPException {

		try {
			switch (updatedItem.getClass().getSimpleName()) {
				case "DMDIIDocument":  addDMDIIDocumentUpdate((DMDIIDocument)updatedItem);
					break;
				case "DMDIIProjectUpdate": addDMDIIProjectUpdate((DMDIIProjectUpdate)updatedItem);
					break;
				case "DMDIIProject": addUpdateForDMDIIProject((DMDIIProject)updatedItem);
					break;
				case "Organization": addUpdateForOrganization((Organization)updatedItem);
					break;
				case "DMDIIMember": addUpdateForDMDIIMember((DMDIIMember)updatedItem);
					break;
				default: break;
			}
		} catch (SQLException e) {

		}

  }

	// This function is for updates on existing entries, and therefore does do a
	//	comparison with the 'original' object
	public void createRecentUpdate(Object updatedItem, Object originalItem) throws HTTPException {

		Class itemClass = originalItem.getClass();
		Method[] methods = itemClass.getMethods();

		for(int i = 0; i < methods.length; i++) {
			String methName = methods[i].getName();
			if (methName.startsWith("get")) {
				String origValue = valueToString(methods[i], originalItem);
				String newValue = valueToString(methods[i], updatedItem);
				if (!origValue.equals(newValue)) {
					String attributeName = methName.replace("get","");
					String description = attributeName+" has been updated";
					String internalDescription = "from "+origValue+" to "+newValue;
					createRecentUpdate(updatedItem, description, internalDescription, attributeName);
				}
			}
		}

	}

	// This function is step two for newly-created updates, and directs the update to the
	//	class-specific method required
	public void createRecentUpdate(Object updatedItem, String description, String internalDescription, String attributeName) throws HTTPException {
		try {
			switch (updatedItem.getClass().getSimpleName()) {
				case "DMDIIDocument":  addDMDIIDocumentUpdate((DMDIIDocument)updatedItem, description, internalDescription, attributeName);
					break;
				case "DMDIIProjectUpdate": addDMDIIProjectUpdate((DMDIIProjectUpdate)updatedItem, description, internalDescription, attributeName);
					break;
				case "DMDIIProject": addUpdateForDMDIIProject((DMDIIProject)updatedItem, description, internalDescription, attributeName);
					break;
				case "Organization": addUpdateForOrganization((Organization)updatedItem, description, internalDescription, attributeName);
					break;
				// case "DMDIIMember": addUpdateForDMDIIMember((DMDIIMember)updatedItem, description, internalDescription, attributeName);
				// 	break;
				default: break;
			}
		} catch (SQLException e) {

		}
	}


	private String valueToString(Method method, Object item) {

		try {
			Object methodReturn = method.invoke(item);

			// If the method returns nothing, just return a blank string
			if (methodReturn == null) {
				return "";
			}

			Class returnClass = methodReturn.getClass();

			// If the return of the method is another hibernate object, call getId to
			//	return its id
			if (BaseEntity.class.isAssignableFrom(returnClass)) {
				return ((BaseEntity)methodReturn).getId().toString();
			}

			// If the return of the method is a date, return epoch
			if (Date.class.isAssignableFrom(returnClass)) {
				return Long.toString(((Date)methodReturn).getTime());
			}

			// Once we've covered the above, if the class isn't a string, bool or number
			//	of some kind, return blank string.
			//	TODO be able to deal with collections?
			if (!Number.class.isAssignableFrom(returnClass) && !String.class.isAssignableFrom(returnClass) && !Boolean.class.isAssignableFrom(returnClass)) {
				return "";
			}

			// Otherwise, return the method's return as a string -- if it can be formatted as a number,
			//	do that to avoid mismatches on decimals, etc.
			try {
				Number num = NumberFormat.getInstance().parse(methodReturn.toString());
				return num.toString();
			} catch (ParseException e) {
				return methodReturn.toString();
			}

		} catch (IllegalAccessException|InvocationTargetException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return "";
		}
	}

	// Overloading for adding DMDII Documents
	private void addDMDIIDocumentUpdate(DMDIIDocument dmdiiDocument) throws SQLException {
		String description = dmdiiDocument.getDocumentName();
		String internalDescription = "";
		String attributeName = "";
		addDMDIIDocumentUpdate(dmdiiDocument, description, internalDescription, attributeName);
	}

	private void addDMDIIDocumentUpdate(DMDIIDocument dmdiiDocument, String description, String internalDescription, String attributeName) throws SQLException {

		int parentId;

		// for DMDII docs with no parent, don't add an update
		if (dmdiiDocument.getDmdiiProject() != null) {
			parentId = dmdiiDocument.getDmdiiProject().getId();
		} else {
			return;
		}

		String updateType = dmdiiDocument.getClass().getSimpleName();
		int updateId = dmdiiDocument.getId();
		int userId = dmdiiDocument.getOwner().getId();

		try {
			insertUpdate(updateType, updateId, parentId, description, userId, internalDescription, attributeName);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	// Overloading for adding DMDII Project Updates
	private void addDMDIIProjectUpdate(DMDIIProjectUpdate dmdiiProjectUpdate) throws SQLException {
			String description = dmdiiProjectUpdate.getTitle();
			String internalDescription = "";
			String attributeName = "";
			addDMDIIProjectUpdate(dmdiiProjectUpdate, description, internalDescription, attributeName);
	}


	private void addDMDIIProjectUpdate(DMDIIProjectUpdate dmdiiProjectUpdate, String description, String internalDescription, String attributeName) throws SQLException {
			String updateType = dmdiiProjectUpdate.getClass().getSimpleName();
			int updateId = dmdiiProjectUpdate.getId();
			int parentId = dmdiiProjectUpdate.getProject().getId();
			int userId = dmdiiProjectUpdate.getCreator().getId();

		try {
			insertUpdate(updateType, updateId, parentId, description, userId, internalDescription, attributeName);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	// Overloading for adding DMDII Projects (or updating the projects themselves)
	private void addUpdateForDMDIIProject(DMDIIProject dmdiiProject) throws SQLException {
			String description = dmdiiProject.getProjectTitle();
			String internalDescription = "";
			String attributeName = "";
			addUpdateForDMDIIProject(dmdiiProject, description, internalDescription, attributeName);
	}

	private void addUpdateForDMDIIProject(DMDIIProject dmdiiProject, String description, String internalDescription, String attributeName) throws SQLException {
			String updateType = dmdiiProject.getClass().getSimpleName();
			int updateId = dmdiiProject.getId();
			int parentId = updateId;
			// TODO -- need to update this to pull in who actually creaetd the project
			int userId;
			if (dmdiiProject.getPrincipalPointOfContact() != null) {
				userId = dmdiiProject.getPrincipalPointOfContact().getId();
			} else {
				userId = 0;
			}

		try {
			insertUpdate(updateType, updateId, parentId, description, userId, internalDescription, attributeName);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	// Overloading for adding Organizations (or updating the organizations themselves)
	private void addUpdateForOrganization(Organization organization) throws SQLException {
			String description = organization.getName();
			String internalDescription = "";
			String attributeName = "";
			addUpdateForOrganization(organization, description, internalDescription, attributeName);
	}

	private void addUpdateForOrganization(Organization organization, String description, String internalDescription, String attributeName) throws SQLException {
			String updateType = organization.getClass().getSimpleName();
			int updateId = organization.getId();
			int parentId = updateId;
			// TODO update organization creation/modification to capture who is updating what
			//	right now, appears we have no owner information
			int userId = 0;

		try {
			insertUpdate(updateType, updateId, parentId, description, userId, internalDescription, attributeName);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}


	// Overloading for adding DMDIIMember
	private void addUpdateForDMDIIMember(DMDIIMember dmdiimember) throws SQLException {
			String description = dmdiimember.getOrganization().getName();
			String internalDescription = "";
			String attributeName = "";
			addUpdateForDMDIIMember(dmdiimember, description, internalDescription, attributeName);
	}

	private void addUpdateForDMDIIMember(DMDIIMember dmdiimember, String description, String internalDescription, String attributeName) throws SQLException {
			String updateType = dmdiimember.getClass().getSimpleName();
			int updateId = dmdiimember.getOrganization().getId();
			int parentId = updateId;
			// TODO update dmdiimember creation/modification to capture who is updating what
			//	right now, appears we have no owner information
			int userId = 0;

		try {
			insertUpdate(updateType, updateId, parentId, description, userId, internalDescription, attributeName);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

	}

	private void insertUpdate(String updateType, int updateId, int parentId, String description, int userId, String internalDescription, String attributeName) throws SQLException {

		Long currentTime = System.currentTimeMillis() / 1000L;

		try {
			String query = "INSERT INTO recent_update (update_date, update_type, update_id, parent_id, description, user_id, internal_description, attribute_name)"
			+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			// preparedStatement.setTimestamp(1, date);
			preparedStatement.setLong(1, currentTime);
			preparedStatement.setString(2, updateType);
			preparedStatement.setInt(3, updateId);
			preparedStatement.setInt(4, parentId);
			preparedStatement.setString(5, description);
			preparedStatement.setInt(6, userId);
			preparedStatement.setString(7, internalDescription);
			preparedStatement.setString(8, attributeName);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
		}
	}

}
