package org.dmc.services.discussions;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;

public class DiscussionListDao {

	private final String logTag = DiscussionListDao.class.getName();
	private ResultSet resultSet;

	public ArrayList<Discussion> getDiscussionList(String userEPPN, int limit, String order, String sort) throws HTTPException {

		ArrayList<Discussion> discussions = new ArrayList<Discussion>();
        
        try {
            resultSet = DBConnector.executeQuery("SELECT * FROM forum_messages ORDER BY " + sort + " " + order + " LIMIT " + limit);

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("message_id"));
                String title  = resultSet.getString("body");
                String message = resultSet.getString("body");
                String createdBy = resultSet.getString("reply_to");
                BigDecimal createdAt = new java.math.BigDecimal(String.valueOf(resultSet.getInt("time_posted")));
                String accountId = String.valueOf(resultSet.getInt("user_id"));
                String projectId = String.valueOf(resultSet.getInt("topic_id"));

                Discussion discussion = new Discussion.DiscussionBuilder(id, title)
                	.message(message)
                	.createdBy(createdBy)
                	.createdAt(createdAt)
                	.accountId(accountId)
                	.projectId(projectId)
                .build();
                
                discussions.add(discussion);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        
        return discussions;		
	}
}
