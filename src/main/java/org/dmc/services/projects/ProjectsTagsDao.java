package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;


public class ProjectsTagsDao {

	private Connection connection;
	private final String logTag = ProjectsTagsDao.class.getName();
	private ResultSet resultSet;

	public ProjectsTagsDao() {
	}

	public ProjectTag createProjectsTags(ProjectTag tag, String userEPPN) throws DMCServiceException {
		try {
			
			int userId = UserDao.getUserID(userEPPN);
			int memberId = Integer.parseInt(member.getProfileId());
			int projectId = Integer.parseInt(member.getProjectId());
			return new ProjectTag();
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	}
}
