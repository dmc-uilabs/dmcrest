package org.dmc.services.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class CompanyVideoDao {

	private final String logTag = CompanyVideoDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;
	
    public ArrayList<CompanyVideo> getCompanyVideos(int companyID, String userEPPN) throws HTTPException {
        ArrayList<CompanyVideo> videos = new ArrayList<CompanyVideo>();        
        ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all videos of the company: " + companyID);
        try {
        	String query = "SELECT c.id, c.caption title, c.video_link link, ov.organization_id "
        			+ "FROM organization_video ov "
        			+ "JOIN common_video c ON c.id = ov.video_id "
        			+ "WHERE ov.organization_id = ?";
        	
        	PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, companyID);
			this.resultSet = preparedStatement.executeQuery();

            while (this.resultSet.next()) {
            	int id = this.resultSet.getInt("id");
            	int companyId = this.resultSet.getInt("organization_id");
            	String title = this.resultSet.getString("title");
            	String link = this.resultSet.getString("link");
                CompanyVideo video = new CompanyVideo(id, companyId, title, link);
                videos.add(video);
            }
        } catch (Exception e) {
            throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value()); 
        }
        return videos;
	}
    
	public Id createCompanyVideo(CompanyVideo video, String userEPPN) throws HTTPException {

		String query;
		int videoId = -99999, videoTypeId = -1;
		PreparedStatement statement;
		Util util = Util.getInstance();

		try {

			
			// Check that the user creating the companyVideo is an administrator or the owner
			/**
			 ** Checks disabled until members for companies are tracked
			CompanyDao companyDao = new CompanyDao();
			if (!(companyDao.isOwnerOfCompany(video.getCompanyId(), userIdEPPN) || companyDao.isAdminOfCompany(video.getCompanyId(), userIdEPPN))) {
				ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to add videos for company " + video.getCompanyId());
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
			}
			*/
			
			Connection connection = DBConnector.connection();
			connection.setAutoCommit(false);
			
			query = "SELECT id FROM common_video_image_type WHERE type_desc = ?";
	        statement = DBConnector.prepareStatement(query);
	        statement.setString(1, "organization_video"); 
	        ResultSet resultSet  = statement.executeQuery();
	        if (resultSet.next()) {
	        	videoTypeId = resultSet.getInt("id");
	        	
				query = "INSERT INTO common_video  (length, caption, video_link) VALUES (?, ?, ?)";
				statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, 0);
				statement.setString(2, video.getTitle());
				statement.setString(3, video.getLink());
				statement.executeUpdate();

				videoId = util.getGeneratedKey(statement, "id");
				ServiceLogger.log(logTag, "VIDEO ID: " + videoId);

				query = "INSERT INTO organization_video (organization_id, video_type, video_id) VALUES (?, ?, ?) ";
				statement = DBConnector.prepareStatement(query);
				
				ServiceLogger.log(logTag,  "ORganization ID: " + video.getCompanyId());
				
				statement.setInt(1, video.getCompanyId());
				statement.setInt(2, videoTypeId);
				statement.setInt(3, videoId);
				statement.executeUpdate();
	        }

	        connection.commit();

		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction createCompanyVideo Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {}
			}
		}

		return new Id.IdBuilder(videoId).build();
	}
    
	public Id deleteCompanyVideo(int companyId, int videoId, String userEPPN) throws HTTPException {
		
		int id = -1;
		String query;
		connection = DBConnector.connection();
		PreparedStatement statement;
		Util util = Util.getInstance();
		Boolean resetAutoCommit = true;
		
		try {
			
			if (!connection.getAutoCommit()) {
				resetAutoCommit = false;
			}
			
			if (videoId != -1) {
				
				query = "SELECT organization_id FROM organization_video WHERE video_id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, videoId); 
		        ResultSet resultSet  = statement.executeQuery();
		        
		        if (resultSet.next()) {
		        	
					// Check that the user deleting the company video is an administrator or the owner
					/**
					 ** Checks disabled until members for companies are tracked
					 *
					companyId = resultSet.getInt("organization_id");
					if (!(companyDao.isOwnerOfCompany(companyId, userIdEPPN) || isAdminOfCompany(companyId, userIdEPPN))) {
						ServiceLogger.log(logTag, "User " + userEPPN + " is not authorized to add administrators for company " + companyId);
						throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
					}
					*/
		        	
					query = "DELETE FROM organization_video WHERE video_id = ?";
			        statement = DBConnector.prepareStatement(query);
			        statement.setInt(1, videoId); 
			        statement.executeUpdate();
			        
					query = "DELETE FROM common_video WHERE id = ?";
			        statement = DBConnector.prepareStatement(query);
			        statement.setInt(1, videoId); 
			        statement.executeUpdate();
		        }
		        
			} else if (companyId != -1) {
				ArrayList<String> videoIds = new ArrayList<String>();
				query = "SELECT video_id FROM organization_video WHERE organization_id = ?";
		        statement = DBConnector.prepareStatement(query);
		        statement.setInt(1, companyId); 
		        
		        ResultSet resultSet  = statement.executeQuery();
		        
		        String queryOrg = "DELETE FROM organization_video WHERE video_id IN (";
		        query = "DELETE FROM common_video WHERE id IN (";
		        Boolean firstAdd = true;
		        while (resultSet.next()) {
		        	videoIds.add(String.valueOf(resultSet.getInt("video_id")));
		        	query += (firstAdd) ?  "?" : ", ?";
		        	queryOrg += (firstAdd) ?  "?" : ", ?";
		        	firstAdd = false;
		        }
		        query += ")";
		        queryOrg += ")";
		        
		        String ids = StringUtils.join(videoIds.iterator(), ","); 
		        
		        if (videoIds.size() > 0) {
		        	statement = DBConnector.prepareStatement(queryOrg);
		        	for (int i=0; i<videoIds.size(); i++) {
		        		statement.setInt(i+1, Integer.parseInt(videoIds.get(i))); 
		        	}
			        statement.executeUpdate();	
			        
		        	statement = DBConnector.prepareStatement(query);
		        	for (int i=0; i<videoIds.size(); i++) {
		        		statement.setInt(i+1, Integer.parseInt(videoIds.get(i))); 
		        	}
			        statement.executeUpdate();	
		        }
			}
	        
			connection.commit();
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, "EXCEPTION HER: " + e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction deleteCompanyVideo Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			return null;
		} finally {
			if (connection != null) {
				try {
					if (resetAutoCommit) {
						connection.setAutoCommit(true);
					}
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
		}

		return new Id.IdBuilder(id).build();
	}
	
}