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

//			version integer NOT NULL,
//			model_id text NOT NULL,
//			interface_id_str text NOT NULL,
//			type text NOT NULL,
//			name text NOT NULL,
//			service_id integer NOT NULL,
//			server_id integer NOT NULL
			
			PreparedStatement preparedStatementDomeInterfaceQuery = DBConnector.prepareStatement(addDomeInterfaceQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementDomeInterfaceQuery.setInt(1, postUpdateDomeInterface.getVersion());
			preparedStatementDomeInterfaceQuery.setString(2, postUpdateDomeInterface.getModelId());
			preparedStatementDomeInterfaceQuery.setString(3, postUpdateDomeInterface.getInterfaceId());
			preparedStatementDomeInterfaceQuery.setString(4, postUpdateDomeInterface.getType());
			preparedStatementDomeInterfaceQuery.setString(5, postUpdateDomeInterface.getName());
			
			//ToDo: deal with Path
			
			preparedStatementDomeInterfaceQuery.setInt(6, postUpdateDomeInterface.getServiceId());
			

			String domeServerStr = postUpdateDomeInterface.getDomeServer();
			preparedStatementDomeInterfaceQuery.setInt(7, Integer.parseInt(domeServerStr));

			int rowsAffected_interface = preparedStatementDomeInterfaceQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				//ToDo: need to change error
				throw new DMCServiceException(DMCError.MemberNotAssignedToProject, "unable to add dome interface " + postUpdateDomeInterface.toString());
			}
			int id = util.getGeneratedKey(preparedStatementDomeInterfaceQuery, "interface_id");

			
			retObj.setId(Integer.toString(id));
			retObj.setDomeServer(postUpdateDomeInterface.getDomeServer());
			retObj.setInterfaceId(postUpdateDomeInterface.getInterfaceId());
			retObj.setModelId(postUpdateDomeInterface.getModelId());
			retObj.setName(postUpdateDomeInterface.getName());
			//retObj.setPath(postUpdateDomeInterface.getPath());
			retObj.setPath(null); //TO DO
			retObj.setServiceId(new BigDecimal(Integer.toString(postUpdateDomeInterface.getServiceId())));
			retObj.setType(postUpdateDomeInterface.getType());
			retObj.setVersion(new BigDecimal(Integer.toString(postUpdateDomeInterface.getVersion())));

			/*ListIterator<Integer> pathListIter = postUpdateDomeInterface.getPath().listIterator();
			// may need to loop over the insert
			String addDomeInterfacePathQuery = "INSERT into dome_interface_path (interface_id, path) values ( ?, ?)";
			
			while(pathListIter.hasNext()) {
				//    interface_id integer NOT NULL,
				//			path integer NOT NULL
				PreparedStatement preparedStatement = DBConnector.prepareStatement(addDomeInterfaceQuery);
				preparedStatement.setInt(1, interface_id);
				preparedStatement.setInt(2, pathListIter.next());

				int rowsAffected_interfacePath = preparedStatementDomeInterfaceQuery.executeUpdate();
				if (rowsAffected_interfacePath != 1) {
					connection.rollback();
					//ToDo: need to change error
					throw new DMCServiceException(DMCError.MemberNotAssignedToProject, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

			}*/
			
			
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
		GetDomeInterface retObj = new GetDomeInterface();

				
		try {
			// let's start a transaction
			connection.setAutoCommit(false);

			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE interface_id=" + domeInterfaceId.toString();


			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			boolean completed = preparedStatement.execute();
			
			
			ResultSet resultSet = preparedStatement.getResultSet();
						
			if(resultSet.next()) {
			
				retObj.setDomeServer(Integer.toString(resultSet.getInt("server_id")));
				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));
				retObj.setPath(null); //TO DO
				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));
				
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
	
}