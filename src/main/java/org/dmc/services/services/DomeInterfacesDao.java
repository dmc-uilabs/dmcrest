package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectMember;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.sharedattributes.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

class DomeInterfacesDao {
	
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
				//ToDo: need to change error
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

			/*ListIterator<Integer> pathListIter = postUpdateDomeInterface.getPath().listIterator();
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
					//ToDo: need to change error
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

			}*/
			
			
			retObj.setPath(null);
			
			
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
		
		
		
		return retObj;
	}
	
	
	
	
	
	
	
	
	
	
	//used for GET
	//returns the GetDomeInterface pointed to by the domeInterfaceId
	public GetDomeInterface getDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
		GetDomeInterface retObj = null;
		boolean readSomethingFromTable = false;

				
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE interface_id = ?";


			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			boolean completed = preparedStatement.execute();
			
	
				
			
			
			ResultSet resultSet = preparedStatement.getResultSet();
						
			if(resultSet.next()) {
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
				/*String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();
	
	
				
				preparedStatement = DBConnector.prepareStatement(query);
				completed = preparedStatement.execute();
	
				
				resultSet = preparedStatement.getResultSet();
	
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
	
				while(resultSet.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSet.getInt("path"))));
				}*/
				
				retObj.setPath(null);
			}
			
				
			
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
		
		
		
		
		return retObj;
	}
	
	
	
	
	
	
	
	
	//used for DELETE
	//deletes GetDomeInterface pointed to by domeInterfaceId
	public void deleteDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

				
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			
			
			String domeInterfacesQuery = "DELETE FROM service_interface WHERE interface_id = ?";
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			int rowsAffected = preparedStatement.executeUpdate();
			

			
			
			if (rowsAffected != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}
			
			

			/*String query = "DELETE FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();


			
			preparedStatement = DBConnector.prepareStatement(query);
			rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}
			
			*/
			
				
			
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
		
		
		
		
		
	}
	
	
	
	
	
	
	
	//used for PATCH
	//updates PostUpdateDomeInterface into database and returns GetDomeInterface
	public GetDomeInterface updateDomeInterface(BigDecimal domeInterfaceId, PostUpdateDomeInterface postUpdateDomeInterface) throws DMCServiceException {
		
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
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
				//ToDo: need to change error
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

			/*
			 * 
			 * TO DO
			 * 1. Delete old path values in service_interface_path
			 * 2. Rewrite path values
			 * 
			 * */
			
			
			retObj.setPath(null);
			
			
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
		
		
		
		return retObj;
		
		
	}
	
}