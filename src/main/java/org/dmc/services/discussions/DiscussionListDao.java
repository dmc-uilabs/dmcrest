package org.dmc.services.discussions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;

import java.util.Date;

import javax.xml.ws.http.HTTPException;

public class DiscussionListDao {

	private final String logTag = DiscussionListDao.class.getName();
	private ResultSet resultSet;

	public ArrayList<Discussion> getDiscussionList(String userEPPN) throws HTTPException {

		ArrayList<Discussion> discussions = new ArrayList<Discussion>();
        return discussions;
		/*
		int id = 0;
		String text = "";
		String full_name = "";
		String avatar = "";
		int projectId = 0;
		long dateInt = 0;
		String date = "";

		String query = "SELECT h.comment_id AS id, " + "h.comment AS text, h.time_posted AS time, u.realname AS name, " + "h.ref_id AS pid, g.group_name as title FROM home_comments h JOIN "
				+ "users u ON u.user_id = h.user_id JOIN groups g ON h.ref_id = g.group_id";

		try {
			return discussions;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

		return null;
		*/

	}
}
