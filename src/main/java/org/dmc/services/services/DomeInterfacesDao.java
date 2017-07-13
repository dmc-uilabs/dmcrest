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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class DomeInterfacesDao {

	private final String logTag = DomeInterfacesDao.class.getName();

	// used for POST
	// stores PostUpdateDomeInterface into database and returns GetDomeInterface
	public GetDomeInterface createDomeInterface(PostUpdateDomeInterface postUpdateDomeInterface, String userEPPN) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
		GetDomeInterface retObj = new GetDomeInterface();

		try {
			connection.setAutoCommit(false);

			String addDomeInterfaceQuery = "INSERT into service_interface (version, model_id, interface_id_str, type, name, service_id, server_id) values ( ?, ?, ?, ?, ?, ?, ? )";

			PreparedStatement preparedStatementDomeInterfaceQuery = DBConnector.prepareStatement(addDomeInterfaceQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementDomeInterfaceQuery.setInt(1, postUpdateDomeInterface.getVersion());
			preparedStatementDomeInterfaceQuery.setString(2, postUpdateDomeInterface.getModelId());
			preparedStatementDomeInterfaceQuery.setString(3, postUpdateDomeInterface.getInterfaceId());
			preparedStatementDomeInterfaceQuery.setString(4, postUpdateDomeInterface.getType());
			preparedStatementDomeInterfaceQuery.setString(5, postUpdateDomeInterface.getName());

			preparedStatementDomeInterfaceQuery.setInt(6, postUpdateDomeInterface.getServiceId());

			preparedStatementDomeInterfaceQuery.setInt(7, Integer.parseInt(postUpdateDomeInterface.getInterfaceId()));

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

			while (pathListIter.hasNext()) {
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

			Iterator<DomeModelParam> inParamsListIter = postUpdateDomeInterface.getInParams().values().iterator();
			String inParamsQuery = "INSERT into service_interface_parameter (interface_id, name, type, unit, category, default_value, parameter_id_txt, parameter_position, input_parameter, instancename) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int i = 1;

			while (inParamsListIter.hasNext()) {
				DomeModelParam tempInParam = inParamsListIter.next();

				PreparedStatement preparedStatement = DBConnector.prepareStatement(inParamsQuery);
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, tempInParam.getName());
				preparedStatement.setString(3, tempInParam.getType());
				preparedStatement.setString(4, tempInParam.getUnit());
				preparedStatement.setString(5, tempInParam.getCategory());
				preparedStatement.setString(6, tempInParam.getValue().toString());
				preparedStatement.setString(7, tempInParam.getParameterid());
				preparedStatement.setInt(8, i);
				preparedStatement.setBoolean(9, true);
				preparedStatement.setString(10, tempInParam.getInstancename());

				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

				i++;
			}

			retObj.setInParams(postUpdateDomeInterface.getInParams());

			Iterator<DomeModelParam> outParamsListIter = postUpdateDomeInterface.getOutParams().values().iterator();
			String outParamsQuery = "INSERT into service_interface_parameter (interface_id, name, type, unit, category, default_value, parameter_id_txt, parameter_position, input_parameter, instancename) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			i = 1;

			while (outParamsListIter.hasNext()) {
				DomeModelParam tempOutParam = outParamsListIter.next();

				PreparedStatement preparedStatement = DBConnector.prepareStatement(outParamsQuery);
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, tempOutParam.getName());
				preparedStatement.setString(3, tempOutParam.getType());
				preparedStatement.setString(4, tempOutParam.getUnit());
				preparedStatement.setString(5, tempOutParam.getCategory());
				preparedStatement.setString(6, tempOutParam.getValue().toString());
				preparedStatement.setString(7, tempOutParam.getParameterid());
				preparedStatement.setInt(8, i);
				preparedStatement.setBoolean(9, false);
				preparedStatement.setString(10, tempOutParam.getInstancename());

				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

				i++;
			}

			retObj.setOutParams(postUpdateDomeInterface.getOutParams());

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

	// used for GET
	// returns the GetDomeInterface pointed to by the domeInterfaceId
	public GetDomeInterface getDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		boolean readSomethingFromTable = false;
		Integer domeServer = null;

		try {
			connection.setAutoCommit(false);

			String domeInterfacesQuery = "SELECT interface_id, version, model_id, interface_id_str, type, name, service_id, server_id FROM service_interface WHERE interface_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.getResultSet();

			if (resultSet.next()) {
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

				while (resultSet.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSet.getInt("path"))));
				}

				retObj.setPath(newPath);

				query = "SELECT * FROM service_interface_parameter WHERE interface_id=" + domeInterfaceId.toString();
				preparedStatement = DBConnector.prepareStatement(query);
				preparedStatement.execute();
				resultSet = preparedStatement.getResultSet();

				Map<String, DomeModelParam> newInParams = new HashMap<String, DomeModelParam>();
				Map<String, DomeModelParam> newOutParams = new HashMap<String, DomeModelParam>();
				while (resultSet.next()) {
					DomeModelParam tempInParam = new DomeModelParam();
					tempInParam.setName(resultSet.getString("name"));
					tempInParam.setType(resultSet.getString("type"));
					tempInParam.setUnit(resultSet.getString("unit"));
					tempInParam.setCategory(resultSet.getString("category"));
					tempInParam.setValue(resultSet.getString("default_value"));
					tempInParam.setParameterid(resultSet.getString("parameter_id_txt"));
					tempInParam.setInstancename(resultSet.getString("instancename"));

					if (resultSet.getBoolean("input_parameter")) {
						newInParams.put(resultSet.getString("name"), tempInParam);
					} else {
						newOutParams.put(resultSet.getString("name"), tempInParam);
					}
				}
				retObj.setInParams(newInParams);
				retObj.setOutParams(newOutParams);
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

	// used for DELETE
	// deletes GetDomeInterface pointed to by domeInterfaceId
	public void deleteDomeInterface(BigDecimal domeInterfaceId) throws DMCServiceException {
		Connection connection = DBConnector.connection();

		try {
			connection.setAutoCommit(false);

			String query = "DELETE FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();

			PreparedStatement pathPreparedStatement = DBConnector.prepareStatement(query);
			int pathRowsAffected = pathPreparedStatement.executeUpdate();

			if (pathRowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}

			String paramQuery = "DELETE FROM service_interface_parameter WHERE interface_id = ?";
			PreparedStatement paramPreparedStatement = DBConnector.prepareStatement(paramQuery);
			paramPreparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			int paramRowsAffected = paramPreparedStatement.executeUpdate();

			if (paramRowsAffected < 1) {
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

	// used for PATCH
	// updates PostUpdateDomeInterface into database and returns GetDomeInterface
	public GetDomeInterface updateDomeInterface(BigDecimal domeInterfaceId, PostUpdateDomeInterface postUpdateDomeInterface) throws DMCServiceException {

		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = new GetDomeInterface();
		Integer domeServer = null;

		try {
			connection.setAutoCommit(false);

			String getServerQuery = "SELECT server_id FROM servers WHERE url = ?";
			PreparedStatement preparedStatementGetServer = DBConnector.prepareStatement(getServerQuery);
			preparedStatementGetServer.setString(1, postUpdateDomeInterface.getDomeServer());
			preparedStatementGetServer.execute();
			ResultSet resultSetGetServer = preparedStatementGetServer.getResultSet();
			if (resultSetGetServer.next()) {
				domeServer = resultSetGetServer.getInt("server_id");
			}

			String updateQuery = "UPDATE service_interface SET version=?, model_id=?, interface_id_str=?, type=?, name=?, server_id=? WHERE interface_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(updateQuery);
			preparedStatement.setInt(1, postUpdateDomeInterface.getVersion());
			preparedStatement.setString(2, postUpdateDomeInterface.getModelId());
			preparedStatement.setString(3, postUpdateDomeInterface.getInterfaceId());
			preparedStatement.setString(4, postUpdateDomeInterface.getType());
			preparedStatement.setString(5, postUpdateDomeInterface.getName());

			preparedStatement.setInt(6, domeServer);

			preparedStatement.setInt(7, domeInterfaceId.intValue());

			int rowsAffected_interface = preparedStatement.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to update dome interface " + postUpdateDomeInterface.toString());
			}

			// Delete old path values
			String query = "DELETE FROM service_interface_path WHERE interface_id=" + domeInterfaceId.toString();

			PreparedStatement pathPreparedStatement = DBConnector.prepareStatement(query);
			int pathRowsAffected = pathPreparedStatement.executeUpdate();

			if (pathRowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}

			// Write (potentially new) path values
			ListIterator<Integer> pathListIter = postUpdateDomeInterface.getPath().listIterator();
			List<BigDecimal> retPathList = new ArrayList<BigDecimal>();

			String addDomeInterfacePathQuery = "INSERT into service_interface_path (interface_id, path) values ( ?, ?)";

			while (pathListIter.hasNext()) {
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

			// Delete old parameters
			String paramQuery = "DELETE FROM service_interface_parameter WHERE interface_id = ?";
			PreparedStatement paramPreparedStatement = DBConnector.prepareStatement(paramQuery);
			paramPreparedStatement.setInt(1, new Integer(domeInterfaceId.intValue()));
			int paramRowsAffected = paramPreparedStatement.executeUpdate();

			if (paramRowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to remove dome interface " + domeInterfaceId);
			}

			// Write (potentially) new parameters
			Iterator<DomeModelParam> inParamsListIter = postUpdateDomeInterface.getInParams().values().iterator();
			String inParamsQuery = "INSERT into service_interface_parameter (interface_id, name, type, unit, category, default_value, parameter_id_txt, parameter_position, input_parameter, instancename) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int i = 1;

			while (inParamsListIter.hasNext()) {
				DomeModelParam tempInParam = inParamsListIter.next();

				PreparedStatement preparedStatementInParams = DBConnector.prepareStatement(inParamsQuery);
				preparedStatementInParams.setInt(1, domeInterfaceId.intValue());
				preparedStatementInParams.setString(2, tempInParam.getName());
				preparedStatementInParams.setString(3, tempInParam.getType());
				preparedStatementInParams.setString(4, tempInParam.getUnit());
				preparedStatementInParams.setString(5, tempInParam.getCategory());
				preparedStatementInParams.setString(6, tempInParam.getValue().toString());
				preparedStatementInParams.setString(7, tempInParam.getParameterid());
				preparedStatementInParams.setInt(8, i);
				preparedStatementInParams.setBoolean(9, true);
				preparedStatementInParams.setString(10, tempInParam.getInstancename());

				int rowsAffected = preparedStatementInParams.executeUpdate();
				if (rowsAffected != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

				i++;
			}

			Iterator<DomeModelParam> outParamsListIter = postUpdateDomeInterface.getOutParams().values().iterator();
			String outParamsQuery = "INSERT into service_interface_parameter (interface_id, name, type, unit, category, default_value, parameter_id_txt, parameter_position, input_parameter, instancename) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			i = 1;

			while (outParamsListIter.hasNext()) {
				DomeModelParam tempOutParam = outParamsListIter.next();

				PreparedStatement preparedStatementOutParams = DBConnector.prepareStatement(outParamsQuery);
				preparedStatementOutParams.setInt(1, domeInterfaceId.intValue());
				preparedStatementOutParams.setString(2, tempOutParam.getName());
				preparedStatementOutParams.setString(3, tempOutParam.getType());
				preparedStatementOutParams.setString(4, tempOutParam.getUnit());
				preparedStatementOutParams.setString(5, tempOutParam.getCategory());
				preparedStatementOutParams.setString(6, tempOutParam.getValue().toString());
				preparedStatementOutParams.setString(7, tempOutParam.getParameterid());
				preparedStatementOutParams.setInt(8, i);
				preparedStatementOutParams.setBoolean(9, false);
				preparedStatementOutParams.setString(10, tempOutParam.getInstancename());

				int rowsAffected = preparedStatementOutParams.executeUpdate();
				if (rowsAffected != 1) {
					connection.rollback();
					throw new DMCServiceException(DMCError.OtherSQLError, "unable to add dome interface " + postUpdateDomeInterface.toString());
				}

				i++;
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
		retObj = getDomeInterface(domeInterfaceId);
		return retObj;
	}

	public List<GetDomeInterface> getDomeInterfacesFromServiceId(BigDecimal serviceId, Integer limit, String order, String sort) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		List<GetDomeInterface> domeInterfaces = new ArrayList<GetDomeInterface>();

		try {
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

				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));
				retObj.setDomeServer(Integer.toString(resultSet.getInt("server_id")));

				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));

				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + retObj.getId();

				PreparedStatement preparedStatementPath = DBConnector.prepareStatement(query);
				preparedStatementPath.execute();

				ResultSet resultSetPath = preparedStatementPath.getResultSet();
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
				while (resultSetPath.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSetPath.getInt("path"))));
				}

				retObj.setPath(newPath);

				query = "SELECT * FROM service_interface_parameter WHERE interface_id=" + retObj.getId();
				PreparedStatement preparedStatementParams = DBConnector.prepareStatement(query);
				preparedStatementParams.execute();
				ResultSet resultSetParams = preparedStatementParams.getResultSet();

				Map<String, DomeModelParam> newInParams = new HashMap<String, DomeModelParam>();
				Map<String, DomeModelParam> newOutParams = new HashMap<String, DomeModelParam>();
				while (resultSetParams.next()) {
					DomeModelParam tempInParam = new DomeModelParam();
					tempInParam.setName(resultSetParams.getString("name"));
					tempInParam.setType(resultSetParams.getString("type"));
					tempInParam.setUnit(resultSetParams.getString("unit"));
					tempInParam.setCategory(resultSetParams.getString("category"));
					tempInParam.setValue(resultSetParams.getString("default_value"));
					tempInParam.setParameterid(resultSetParams.getString("parameter_id_txt"));
					tempInParam.setInstancename(resultSetParams.getString("instancename"));

					if (resultSetParams.getBoolean("input_parameter")) {
						newInParams.put(resultSetParams.getString("name"), tempInParam);
					} else {
						newOutParams.put(resultSetParams.getString("name"), tempInParam);
					}
				}
				retObj.setInParams(newInParams);
				retObj.setOutParams(newOutParams);

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

				retObj.setId(Integer.toString(resultSet.getInt("interface_id")));
				retObj.setInterfaceId(resultSet.getString("interface_id_str"));
				retObj.setModelId(resultSet.getString("model_id"));
				retObj.setName(resultSet.getString("name"));
				retObj.setServiceId(new BigDecimal(resultSet.getInt("service_id")));
				retObj.setType(resultSet.getString("type"));
				retObj.setVersion(new BigDecimal(resultSet.getInt("version")));

				String getServerQuery = "SELECT url FROM servers WHERE server_id = ?";
				PreparedStatement preparedStatementGetServer = DBConnector.prepareStatement(getServerQuery);
				preparedStatementGetServer.setInt(1, new Integer(resultSet.getInt("server_id")));
				preparedStatementGetServer.execute();
				ResultSet resultSetGetServer = preparedStatementGetServer.getResultSet();
				if (resultSetGetServer.next()) {
					retObj.setDomeServer(resultSetGetServer.getString("url"));
				}

				String query = "SELECT interface_id, path FROM service_interface_path WHERE interface_id=" + retObj.getId();

				PreparedStatement preparedStatementPath = DBConnector.prepareStatement(query);
				preparedStatementPath.execute();

				ResultSet resultSetPath = preparedStatementPath.getResultSet();
				List<BigDecimal> newPath = new ArrayList<BigDecimal>();
				while (resultSetPath.next()) {
					newPath.add(new BigDecimal(Integer.toString(resultSetPath.getInt("path"))));
				}

				retObj.setPath(newPath);

				query = "SELECT * FROM service_interface_parameter WHERE interface_id=" + retObj.getId();
				PreparedStatement preparedStatementParams = DBConnector.prepareStatement(query);
				preparedStatementParams.execute();
				ResultSet resultSetParams = preparedStatementParams.getResultSet();

				Map<String, DomeModelParam> newInParams = new HashMap<String, DomeModelParam>();
				Map<String, DomeModelParam> newOutParams = new HashMap<String, DomeModelParam>();
				while (resultSetParams.next()) {
					DomeModelParam tempInParam = new DomeModelParam();
					tempInParam.setName(resultSetParams.getString("name"));
					tempInParam.setType(resultSetParams.getString("type"));
					tempInParam.setUnit(resultSetParams.getString("unit"));
					tempInParam.setCategory(resultSetParams.getString("category"));
					tempInParam.setValue(resultSetParams.getString("default_value"));
					tempInParam.setParameterid(resultSetParams.getString("parameter_id_txt"));
					tempInParam.setInstancename(resultSetParams.getString("instancename"));

					if (resultSetParams.getBoolean("input_parameter")) {
						newInParams.put(resultSetParams.getString("name"), tempInParam);
					} else {
						newOutParams.put(resultSetParams.getString("name"), tempInParam);
					}
				}
				retObj.setInParams(newInParams);
				retObj.setOutParams(newOutParams);

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