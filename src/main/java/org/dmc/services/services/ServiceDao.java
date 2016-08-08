package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.SqlTypeConverterUtility;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.company.CompanyDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ServiceDao {

	private final String logTag = ServiceDao.class.getName();
    private Connection connection = null;
	private ResultSet resultSet = null;

	public Service getService(int requestId, String userEPPN) throws DMCServiceException {
		
		try {
			
			String query = "SELECT * FROM service WHERE service_id = " + requestId;
			Service service = null;
			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next()) {

			    service = readServiceResultSet(resultSet);
								
			}
			return service;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, "unable to GET service " + requestId + ": " + e.getMessage());
		}
	}

    public Service createService(Service requestedBody, String userEPPN) throws DMCServiceException {
        try {
            connection = DBConnector.connection();
            // let's start a transaction
            connection.setAutoCommit(false);

            // look up userID
            int userID = UserDao.getUserID(userEPPN);
			int companyId = CompanyDao.getUserCompanyId(userID);

            String query = "insert into service (organization_id, title, description, owner_id, release_date, service_type, specifications, project_id, from_location, type, parent, published)";
            query += "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, companyId);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3,  requestedBody.getDescription());
            preparedStatement.setInt(4,  userID);
            preparedStatement.setObject(5, SqlTypeConverterUtility.getSqlDate(requestedBody.getReleaseDate()), java.sql.Types.DATE);
            preparedStatement.setString(6, requestedBody.getServiceType());
			preparedStatement.setString(7, requestedBody.getSpecifications());
            preparedStatement.setObject(8, SqlTypeConverterUtility.getInt(requestedBody.getProjectId()), java.sql.Types.INTEGER);
            preparedStatement.setString(9, requestedBody.getFrom());
            preparedStatement.setString(10, requestedBody.getType());
            preparedStatement.setString(11, requestedBody.getParent());
            preparedStatement.executeUpdate();

            query = "select currval('service_service_id_seq') as id";
            resultSet = DBConnector.executeQuery(query);

            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            Service service = getService(id, userEPPN);
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
    public Service patchService(String serviceIdText, Service requestedBody, String userEPPN) throws DMCServiceException {
        try {
            int serviceId = Integer.parseInt(serviceIdText);
            if (serviceId != requestedBody.getId()) {
                throw new DMCServiceException(DMCError.OtherSQLError, "serviceId " + serviceId + " does not match " + requestedBody.getId() + " as expected");
            }
            connection = DBConnector.connection();
            // let's start a transaction
            connection.setAutoCommit(false);

            // look up userID
            int userID = UserDao.getUserID(userEPPN);

            String query = "update service set ";
            query += "organization_id=?, ";
            query += "title=?, ";
            query += "description=?, ";
            query += "release_date=?, ";
            query += "service_type=?, ";
            query += "project_id=?, ";
            query += "from_location=?, ";
            query += "type=?, ";
            query += "parent=?, ";
            query += "published=? ";
            query += "where ";
            query += "service_id=? and ";
            query += "owner_id=?";

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setObject(1, SqlTypeConverterUtility.getInt(requestedBody.getCompanyId()), java.sql.Types.INTEGER);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3,  requestedBody.getDescription());
            preparedStatement.setObject(4, SqlTypeConverterUtility.getSqlDate(requestedBody.getReleaseDate()), java.sql.Types.DATE);
            preparedStatement.setString(5, requestedBody.getServiceType());
            preparedStatement.setObject(6, SqlTypeConverterUtility.getInt(requestedBody.getProjectId()), java.sql.Types.INTEGER);
            preparedStatement.setString(7, requestedBody.getFrom());
            preparedStatement.setString(8, requestedBody.getType());
            preparedStatement.setString(9, requestedBody.getParent());
            preparedStatement.setBoolean(10, requestedBody.getPublished());
            preparedStatement.setInt(11, serviceId);
            preparedStatement.setInt(12, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (1 != rowsAffected) {
                throw new Exception("didn't correctly modify service " + requestedBody.getId());
            }
            Service service = getService(requestedBody.getId(), userEPPN);
            connection.commit();
            return service;
        } catch (Exception e) {
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
    
    public ArrayList<Service> getServiceList() throws DMCServiceException {
        ArrayList<Service> list=new ArrayList<Service>();
        try {
            
            resultSet = DBConnector.executeQuery("SELECT * FROM service");
            
            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }
            
            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get serviceList: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }
    
    public ArrayList<Service> getServiceList(int projectId)  throws DMCServiceException {
        ArrayList<Service> list=new ArrayList<Service>();
        
        try {
            
            resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + projectId);
            
            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }
            
            return list;

        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get services for project " + projectId + ": " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }
    
    public ArrayList<Service> getServiceByComponentList(int componentId) throws DMCServiceException {
        ArrayList<Service> list=new ArrayList<Service>();
        try {
            //ToDo need to determine component ID
            resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + componentId);
            
            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }
            
            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get services by component: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }
    
    public ArrayList<Service> getServices(Integer limit, 
            String order, 
            Integer start, 
            String sort, 
            String titleLike, 
            String serviceType, 
            List<Integer> authors, 
            List<String>ratings, 
            String favorites, 
            List<String> dates, 
            List<String> fromLocations,
            String userEPPN) 
                throws DMCServiceException {
        ArrayList<Service> list=new ArrayList<Service>();
        
        try {
            PreparedStatement preparedStatement = setupGetServicesQuery(limit, order, start, sort, titleLike, serviceType, authors, ratings, favorites, dates, fromLocations, userEPPN);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }
            
            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get services: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

    private PreparedStatement setupGetServicesQuery(Integer limit, 
            String order, 
            Integer start, 
            String sort, 
            String titleLike, 
            String serviceType, 
            List<Integer> authors, 
            List<String>ratings, 
            String favorites, 
            List<String> dates, 
            List<String> fromLocations,
            String userEPPN) 
                throws Exception {
        String query = "SELECT * FROM service";

        ArrayList<String> whereClauses = new ArrayList<String>();
        ArrayList<String> orderByClauses = new ArrayList<String>();
        
        if (null != fromLocations && fromLocations.size() > 0) {
            String fromClause = " from_location in (?";
            // already have first placeholder, so start count from 1 instead of 0
            for (int i = 1; i < fromLocations.size(); ++i) {
                fromClause += ", ?";
            }
            fromClause += ")";
            whereClauses.add(fromClause);
        }
        
        if (null != dates && dates.size() > 0) {
            String datesClause = " ( ";
            for (String dateItem : dates) {
                datesClause += " release_date > now() - INTERVAL ";
                char intervalType = dateItem.charAt(dateItem.length()-1);
                Integer dateCount = Integer.parseInt(dateItem.substring(0, dateItem.length()-1));
                String convertedIntervalType = convertIntervalType(intervalType);
                datesClause += "'" + dateCount + convertedIntervalType + "'";
                datesClause += " OR ";
            }
            if (datesClause.endsWith(" OR ")) {
                datesClause = datesClause.substring(0, datesClause.length() - 4);
            }
            datesClause += ")";
            whereClauses.add(datesClause);
        }
        query += addClauses(whereClauses, " WHERE ", " AND ");
        query += addClauses(orderByClauses, " ORDER BY ", ", ");                    

        ServiceLogger.log(logTag, "query: " + query);

        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
        int parameterIndex = 1;
        for (String location : fromLocations) {
            preparedStatement.setString(parameterIndex, location);
            ServiceLogger.log(logTag, "  parameter " + parameterIndex + " : from " + location);            
            parameterIndex++;
        }
        if (null != start) {
        }
        return preparedStatement;
    }

    private Service readServiceResultSet(ResultSet resultSet) throws SQLException
    {
        Service service = new Service();
        service.setId(resultSet.getInt("service_id"));
        service.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
        if (resultSet.wasNull()) service.setCompanyId(null);
        service.setTitle(resultSet.getString("title"));
        service.setDescription(resultSet.getString("description"));
        service.setOwner(resultSet.getString("owner_id"));
        service.setProfileId(resultSet.getString("owner_id"));  // ToDo: up date
        service.setReleaseDate(resultSet.getDate("release_date"));
        if (resultSet.wasNull()) service.setReleaseDate(null);
		service.setServiceType(resultSet.getString("service_type"));
        service.setType(resultSet.getString("service_type"));
        service.setTags(new ArrayList<String>()); // ToDo: up date
        service.setSpecifications(resultSet.getString("specifications"));

        service.setFeatureImage(new FeatureImage("", ""));
        service.setCurrentStatus(new ServiceCurrentStatus(0, "", ""));
        
        service.setProjectId(Integer.toString(resultSet.getInt("project_id")));
        if (resultSet.wasNull()) {
            service.setProjectId(null);
        }
        service.setFrom(resultSet.getString("from_location"));
        service.setType(resultSet.getString("type"));
        service.setParent(resultSet.getString("parent"));
        service.setPublished(resultSet.getBoolean("published"));

        service.setAverageRun("");
        return service;
    }
    private String addClauses(ArrayList<String> clauses, String start, String connector) {
        String queryClause = "";
        if (clauses.size() > 0) {
            queryClause += start;
            for (String clause : clauses) {
                queryClause += clause;
                queryClause += connector;
            }
            // remove any trailing AND 
            if (queryClause.endsWith(connector)) {
                queryClause = queryClause.substring(0, queryClause.length() - connector.length());
            }
        }
        return queryClause;
    }

    String convertIntervalType(char intervalType) {
        String convertedIntervalType = "";
        switch (intervalType) {
            case 'd': 
                convertedIntervalType = " day "; 
                break;
            case 'm': 
                convertedIntervalType = " month ";
                break;
            case 'y': 
                convertedIntervalType = " year ";
                break;
            default:
                convertedIntervalType = " day ";
                break;
        }
        return convertedIntervalType;
    }
    
}
