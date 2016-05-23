package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
//import org.dmc.services.projects.ProjectMember;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.sharedattributes.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DomeInterfacesDao {
	
	private final String logTag = DomeInterfacesDao.class.getName();

	//used for POST
	//stores PostUpdateDomeInterface into database and returns GetDomeInterface
	public GetDomeInterface createDomeInterface(PostUpdateDomeInterface postUpdateDomeInterface, String userEPPN) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
		GetDomeInterface retObj = new GetDomeInterface();
				
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			// requesting user must be administrator of the project to get the
			// list of members.
			String addDomeInterfaceQuery = "INSERT into service_interface (version, model_id, interface_id_str, type, name, service_id, server_id) values ( ?, ?, ?, ?, ?, ?, ? )";

			PreparedStatement preparedStatementDomeInterfaceQuery = DBConnector.prepareStatement(addDomeInterfaceQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementDomeInterfaceQuery.setInt(1, postUpdateDomeInterface.getVersion());
			preparedStatementDomeInterfaceQuery.setString(2, postUpdateDomeInterface.getModelId());
			preparedStatementDomeInterfaceQuery.setString(3, postUpdateDomeInterface.getInterfaceId());
			preparedStatementDomeInterfaceQuery.setString(4, postUpdateDomeInterface.getType());
			preparedStatementDomeInterfaceQuery.setString(5, postUpdateDomeInterface.getName());
			
			preparedStatementDomeInterfaceQuery.setInt(6, postUpdateDomeInterface.getServiceId());

			String domeServerStr = postUpdateDomeInterface.getDomeServer();
			preparedStatementDomeInterfaceQuery.setInt(7, Integer.parseInt(domeServerStr));

			int rowsAffected_interface = preparedStatementDomeInterfaceQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
			}
			int id = util.getGeneratedKey(preparedStatementDomeInterfaceQuery, "interface_id");

			retObj.setId(Integer.toString(id));
			retObj.setDomeServer(postUpdateDomeInterface.getDomeServer());
			retObj.setInterfaceId(postUpdateDomeInterface.getInterfaceId());
			retObj.setModelId(postUpdateDomeInterface.getModelId());
			retObj.setName(postUpdateDomeInterface.getName());
			retObj.setServiceId(new BigDecimal(Integer.toString(postUpdateDomeInterface.getServiceId())));
			retObj.setType(postUpdateDomeInterface.getType());
			retObj.setVersion(new BigDecimal(Integer.toString(postUpdateDomeInterface.getVersion())));

			ListIterator<Integer> pathListIter = postUpdateDomeInterface.getPath().listIterator();
			List<BigDecimal> retPathList = new ArrayList<BigDecimal>();
			
			String addDomeInterfacePathQuery = "INSERT into service_interface_path (interface_id, path) values ( ?, ?)";

			while(pathListIter.hasNext()) {
				//    interface_id integer NOT NULL,
				//			path integer NOT NULL
				Integer tempPathListInt = pathListIter.next();
				
				retPathList.add(new BigDecimal(tempPathListInt.toString()));
				
				PreparedStatement preparedStatement = DBConnector.prepareStatement(addDomeInterfacePathQuery);
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, tempPathListInt);

				int rowsAffected_interfacePath = preparedStatement.executeUpdate();
				if (rowsAffected_interfacePath != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

			}
			
			retObj.setPath(retPathList);
				
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}			
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}
	
		return retObj;
	}
	
	//used for GET
	//returns the GetDomeInterface pointed to by the domeInterfaceId
	public GetDomeInterface getDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		boolean readSomethingFromTable = false;
		
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE interface_id = ?";
	
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
						
			if(resultSet.next()) {
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
			
			if (readSomethingFromTable) {
				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();	
				preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.execute();
	
				
				resultSet = preparedStatement.getResultSet();
	
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
	
				while(resultSet.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSet.getInt("path"))));
				}
				
				retObj.setPath(newPath);
			}		
			
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}
		return retObj;
	}

	//used for DELETE
	//deletes GetDomeInterface pointed to by domeInterfaceId
	public void deleteDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			String query = "DELETE FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();

			PreparedStatement pathPreparedStatement = DBConnector.prepareStatement(query);
			int pathRowsAffected = pathPreparedStatement.executeUpdate();
			
			if (pathRowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}
				
			String domeInterfacesQuery = "DELETE FROM service_interface WHERE interface_id = ?";
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}
				
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}
		}
	}
	
	//used for PATCH
	//updates PostUpdateDomeInterface into database and returns GetDomeInterface
	public GetDomeInterface updateDomeInterface(BigDecimal domeInterfaceId, PostUpdateDomeInterface postUpdateDomeInterface) throws DMCServiceException {
		
		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = new GetDomeInterface();
				
		try {
			// let's start a transaction
			connection.setAutoCommit(false);
			
			// requesting user must be administrator of the project to get the
			// list of members.
			String updateQuery = "UPDATE service_interface SET version=?, model_id=?, interface_id_str=?, type=?, name=?, service_id=?, server_id=? WHERE interface_id = ?";
	
			PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
			preparedStatement.setInt(1, postUpdateDomeInterface.getVersion());
			preparedStatement.setString(2, postUpdateDomeInterface.getModelId());
			preparedStatement.setString(3, postUpdateDomeInterface.getInterfaceId());
			preparedStatement.setString(4, postUpdateDomeInterface.getType());
			preparedStatement.setString(5, postUpdateDomeInterface.getName());
			
			preparedStatement.setInt(6, postUpdateDomeInterface.getServiceId());
			
			String domeServerStr = postUpdateDomeInterface.getDomeServer();
			preparedStatement.setInt(7, Integer.parseInt(domeServerStr));
			
			preparedStatement.setInt(8, domeInterfaceId.intValue());

			int rowsAffected_interface = preparedStatement.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to update dome interface " + postUpdateDomeInterface.toString());
			}
	
			retObj.setId(domeInterfaceId.toString());
			retObj.setDomeServer(postUpdateDomeInterface.getDomeServer());
			retObj.setInterfaceId(postUpdateDomeInterface.getInterfaceId());
			retObj.setModelId(postUpdateDomeInterface.getModelId());
			retObj.setName(postUpdateDomeInterface.getName());
			retObj.setServiceId(new BigDecimal(Integer.toString(postUpdateDomeInterface.getServiceId())));
			retObj.setType(postUpdateDomeInterface.getType());
			retObj.setVersion(new BigDecimal(Integer.toString(postUpdateDomeInterface.getVersion())));

			//Delete old path values
			String query = "DELETE FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();

			PreparedStatement pathPreparedStatement = DBConnector.prepareStatement(query);
			int pathRowsAffected = pathPreparedStatement.executeUpdate();
			
			if (pathRowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}
			
			//Write (potentially new) path values
			ListIterator<Integer> pathListIter = postUpdateDomeInterface.getPath().listIterator();
			List<BigDecimal> retPathList = new ArrayList<BigDecimal>();
			
			String addDomeInterfacePathQuery = "INSERT into service_interface_path (interface_id, path) values ( ?, ?)";
			
			while(pathListIter.hasNext()) {
				Integer tempPathListInt = pathListIter.next();
				
				retPathList.add(new BigDecimal(tempPathListInt.toString()));
				
				PreparedStatement preparedStatementPatchPath = DBConnector.prepareStatement(addDomeInterfacePathQuery);
				preparedStatementPatchPath.setInt(1, new Integer(domeInterfaceId.toString()));
				preparedStatementPatchPath.setInt(2, tempPathListInt);

				int rowsAffected_interfacePath = preparedStatementPatchPath.executeUpdate();
				if (rowsAffected_interfacePath != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

			}

			retObj.setPath(retPathList);

		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}		
		return retObj;
	}
	
	public List<GetDomeInterface> getDomeInterfacesFromServiceId(BigDecimal serviceId, Integer limit, String order, String sort) throws DMCServiceException {
    	Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		List<GetDomeInterface> domeInterfaces = new ArrayList<GetDomeInterface>();
		
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
			
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
			
			while (resultSet.next()) {
				retObj = new GetDomeInterface();
				
				
				retObj.setDomeServer(Integer.toString(resultSet.getInt("server_id")));
				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));

				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));
				
				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + retObj.getId();
				
				PreparedStatement preparedStatementPath = DBConnector.prepareStatement(query);
				preparedStatementPath.execute();
				
				ResultSet resultSetPath = preparedStatementPath.getResultSet();
	
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
	
				while(resultSetPath.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSetPath.getInt("path"))));
				}
				
				retObj.setPath(newPath);
				
				domeInterfaces.add(retObj);
			}
			
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}
		
    	return domeInterfaces;
    }
	
	
	public List<GetDomeInterface> getDomeInterfacesFromInterfaceIdStr(String interface_id_str) throws DMCServiceException {
    	Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		List<GetDomeInterface> domeInterfaces = new ArrayList<GetDomeInterface>();
		
		try {
			// let's start a transaction
			connection.setAutoCommit(false);
			
			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE interface_id_str = ?";
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setString(1, interface_id_str);
			
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
			
			while (resultSet.next()) {
				retObj = new GetDomeInterface();
				
				
				retObj.setDomeServer(Integer.toString(resultSet.getInt("server_id")));
				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));

				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));
				
				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + retObj.getId();
				
				PreparedStatement preparedStatementPath = DBConnector.prepareStatement(query);
				preparedStatementPath.execute();
				
				ResultSet resultSetPath = preparedStatementPath.getResultSet();
	
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
	
				while(resultSetPath.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSetPath.getInt("path"))));
				}
				
				retObj.setPath(newPath);
				
				domeInterfaces.add(retObj);
			}
			
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}
		}
		
    	return domeInterfaces;
    }
	
}