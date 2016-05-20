package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.SqlTypeConverterUtility;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.users.UserDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao {

	private final String logTag = ServiceDao.class.getName();
    private Connection connection = null;
	private ResultSet resultSet = null;

	public Service getService(int requestId, String userEPPN) throws DMCServiceException {
		Service service = new Service();
		
		try {
			
			String query = "SELECT * FROM service WHERE service_id = " + requestId;

			resultSet = DBConnector.executeQuery(query);
			
			while (resultSet.next()) {

				service.setId(requestId);
				service.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
				if (resultSet.wasNull()) service.setCompanyId(null);
				service.setTitle(resultSet.getString("title"));
				service.setDescription(resultSet.getString("description"));
				service.setOwner(resultSet.getString("owner_id"));
				service.setProfileId(resultSet.getString("owner_id"));  // ToDo: up date
				service.setReleaseDate(resultSet.getDate("release_date"));
                if (resultSet.wasNull()) service.setReleaseDate(null);
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

            String query = "insert into service (organization_id, title, description, owner_id, release_date, service_type, project_id, from_location, type, parent, published)";
            query += "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setObject(1, SqlTypeConverterUtility.getInt(requestedBody.getCompanyId()), java.sql.Types.INTEGER);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3,  requestedBody.getDescription());
            preparedStatement.setInt(4,  userID);
            preparedStatement.setObject(5, SqlTypeConverterUtility.getSqlDate(requestedBody.getReleaseDate()), java.sql.Types.DATE);
            preparedStatement.setString(6, requestedBody.getServiceType());
            preparedStatement.setObject(7, SqlTypeConverterUtility.getInt(requestedBody.getProjectId()), java.sql.Types.INTEGER);
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
    
    public List<GetDomeInterface> getServiceIdDomeInterfaces(BigDecimal serviceId, Integer limit, String order, String sort) throws DMCServiceException {
    	Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		List<GetDomeInterface> domeInterfaces = new ArrayList<GetDomeInterface>();
		boolean readSomethingFromTable = false;
		
		try {
			// let's start a transaction
			connection.setAutoCommit(false);
			
			ArrayList<String> columnsInServiceInterfaceTable = new ArrayList<String>();
			columnsInServiceInterfaceTable.add("interface_id");
			columnsInServiceInterfaceTable.add("version");
			columnsInServiceInterfaceTable.add("model_id");
			columnsInServiceInterfaceTable.add("interface_id_str");
			columnsInServiceInterfaceTable.add("type");
			columnsInServiceInterfaceTable.add("name");
			columnsInServiceInterfaceTable.add("service_id");
			columnsInServiceInterfaceTable.add("server_id");
			
			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE service_id = ?";
			
			if (sort == null) {
				domeInterfacesQuery += " ORDER BY interface_id";
			} else if (!columnsInServiceInterfaceTable.contains(sort)) {
				domeInterfacesQuery += " ORDER BY interface_id";
			} else {
				domeInterfacesQuery += " ORDER BY " + sort;
			}
			
			if (order == null) {
				domeInterfacesQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				domeInterfacesQuery += " ASC";
			} else {
				domeInterfacesQuery += " " + order;
			}
			
			if (limit == null) {
				domeInterfacesQuery += " LIMIT ALL";
			} else if (limit < 0) {
				domeInterfacesQuery += " LIMIT 0";
			} else {
				domeInterfacesQuery += " LIMIT " + limit;
			}
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(serviceId.intValue()));
			
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println("BEFORE SQL EXECUTE");
			System.out.println(preparedStatement.toString());
			
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
						
			while (resultSet.next()) {
				readSomethingFromTable = true;
				retObj = new GetDomeInterface();
				
				
				retObj.setDomeServer(Integer.toString(resultSet.getInt("server_id")));
				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));

				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));
				
			}
			
			/*if (readSomethingFromTable) {
				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();
	
	
				
				preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.execute();
	
				
				resultSet = preparedStatement.getResultSet();
	
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
	
				while(resultSet.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSet.getInt("path"))));
				}
				
				retObj.setPath(newPath);
			}		*/
			
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {}

		}

    	
    	return domeInterfaces;
    }
}
