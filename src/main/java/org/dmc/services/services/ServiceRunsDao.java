package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;

public class ServiceRunsDao {

	private static final String LOGTAG = ServiceRunsDao.class.getName();

	public GetServiceRun getSingleServiceRun(String id) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		final GetServiceRun serviceRun = new GetServiceRun();
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			connection.setAutoCommit(false);

			String serviceRunQuery = "SELECT * FROM service_run WHERE run_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(serviceRunQuery);
			preparedStatement.setInt(1, Integer.parseInt(id));
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			if (resultSet.next()) {
				final ModelInterface modelInterface = new ModelInterface();
				final Map<String, DomeModelParam> inputParams = new HashMap<String, DomeModelParam>();
				final Map<String, DomeModelParam> outputParams = new HashMap<String, DomeModelParam>();
				final SimplifiedProject simplifiedProject = new SimplifiedProject();

				serviceRun.setId(Integer.toString(resultSet.getInt("run_id")));
				serviceRun.setStatus(new BigDecimal(resultSet.getInt("status")));
				serviceRun.setAccountId(Integer.toString(resultSet.getInt("account_id")));
				serviceRun.setRunBy(Integer.toString(resultSet.getInt("run_by")));
				serviceRun.setServiceId(Integer.toString(resultSet.getInt("service_id")));
				serviceRun.setPercentCompleted(new BigDecimal(resultSet.getInt("percent_complete")));
				serviceRun.setStartDate(df.format(resultSet.getDate("start_date")));
				if (resultSet.getDate("stop_date") != null) {
					serviceRun.setStopDate(df.format(resultSet.getDate("stop_date")));
				}

				String parameterQuery = "SELECT * FROM service_interface_parameter WHERE interface_id = ?";
				PreparedStatement preparedStatementParameter = DBConnector.prepareStatement(parameterQuery);
				preparedStatementParameter.setInt(1, resultSet.getInt("interface_id"));
				preparedStatementParameter.execute();
				ResultSet resultSetParameter = preparedStatementParameter.getResultSet();

				while (resultSetParameter.next()) {
					final DomeModelParam parameter = new DomeModelParam();
					parameter.setName(resultSetParameter.getString("name"));
					parameter.setType(resultSetParameter.getString("type"));
					parameter.setUnit(resultSetParameter.getString("unit"));
					parameter.setCategory(resultSetParameter.getString("category"));
					parameter.setValue(resultSetParameter.getString("default_value"));
					parameter.setParameterid(resultSetParameter.getString("parameter_id_txt"));
					parameter.setInstancename(resultSetParameter.getString("instancename"));

					if (resultSetParameter.getBoolean("input_parameter")) {
						inputParams.put(resultSetParameter.getString("name"), parameter);
					} else {
						outputParams.put(resultSetParameter.getString("name"), parameter);
					}
				}

				modelInterface.setInParams(inputParams);
				modelInterface.setOutParams(outputParams);
				serviceRun.setInterface(modelInterface);

				String projectQuery = "SELECT groups.group_id, groups.group_name FROM service INNER JOIN groups ON service.project_id = groups.group_id WHERE service.service_id = ?";
				PreparedStatement preparedStatementProject = DBConnector.prepareStatement(projectQuery);
				preparedStatementProject.setInt(1, resultSet.getInt("service_id"));
				preparedStatementProject.execute();
				ResultSet resultSetProject = preparedStatementProject.getResultSet();

				if (resultSetProject.next()) {
					simplifiedProject.setId(Integer.toString(resultSetProject.getInt("group_id")));
					simplifiedProject.setTitle(resultSetProject.getString("group_name"));
				}
				serviceRun.setProject(simplifiedProject);

			}

		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(LOGTAG, se.getMessage());
			}

		}

		return serviceRun;
	}

	public GetServiceRun updateServiceRun(String id, GetServiceRun serviceRunObj) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		GetServiceRun retObj = new GetServiceRun();

		try {
			connection.setAutoCommit(false);

			String serviceRunQuery = "UPDATE service_run SET ";

			if (serviceRunObj.getStatus() != null) {
				serviceRunQuery += "status = ?, ";
			}
			if (serviceRunObj.getPercentCompleted() != null) {
				serviceRunQuery += "percent_complete = ? ";
			}

			serviceRunQuery += "WHERE run_id = ?";

			int i = 1;
			PreparedStatement preparedStatement = DBConnector.prepareStatement(serviceRunQuery);
			if (serviceRunObj.getStatus() != null) {
				preparedStatement.setInt(i, serviceRunObj.getStatus().intValue());
				i++;
			}
			if (serviceRunObj.getPercentCompleted() != null) {
				preparedStatement.setInt(i, serviceRunObj.getPercentCompleted().intValue());
				i++;
			}
			preparedStatement.setInt(i, Integer.parseInt(id));
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to update serviceRun " + serviceRunObj.toString());
			}

			retObj = getSingleServiceRun(id);

		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(LOGTAG, se.getMessage());
			}

		}

		return retObj;
	}

	public List<GetServiceRun> getListOfServiceRuns(Integer limit, String order, String sort, ArrayList<String> serviceIdsList) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		final List<GetServiceRun> serviceRuns = new ArrayList<GetServiceRun>();

		try {
			connection.setAutoCommit(false);

			ArrayList<String> columnsInServiceRunTable = new ArrayList<String>();
			columnsInServiceRunTable.add("run_id");
			columnsInServiceRunTable.add("status");
			columnsInServiceRunTable.add("account_id");
			columnsInServiceRunTable.add("run_by");
			columnsInServiceRunTable.add("service_id");
			columnsInServiceRunTable.add("percent_complete");
			columnsInServiceRunTable.add("start_date");
			columnsInServiceRunTable.add("stop_date");
			columnsInServiceRunTable.add("interface_id");
			columnsInServiceRunTable.add("queue_name");

			String serviceRunQuery = "SELECT * FROM service_run";

			if (sort == null) {
				serviceRunQuery += " ORDER BY run_id";
			} else if (!columnsInServiceRunTable.contains(sort)) {
				serviceRunQuery += " ORDER BY run_id";
			} else {
				serviceRunQuery += " ORDER BY " + sort;
			}

			if (order == null) {
				serviceRunQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				serviceRunQuery += " ASC";
			} else {
				serviceRunQuery += " " + order;
			}

			PreparedStatement preparedStatement = DBConnector.prepareStatement(serviceRunQuery);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			int counter = 0;
			while (resultSet.next() && (limit == null || counter < limit)) {
				if (serviceIdsList == null || serviceIdsList.contains(Integer.toString(resultSet.getInt("service_id")))) {
					final GetServiceRun singleServiceRun = new GetServiceRun();
					final ModelInterface modelInterface = new ModelInterface();
					final Map<String, DomeModelParam> inputParams = new HashMap<String, DomeModelParam>();
					final Map<String, DomeModelParam> outputParams = new HashMap<String, DomeModelParam>();
					final SimplifiedProject simplifiedProject = new SimplifiedProject();
					counter++;

					singleServiceRun.setId(Integer.toString(resultSet.getInt("run_id")));
					singleServiceRun.setStatus(new BigDecimal(resultSet.getInt("status")));
					singleServiceRun.setAccountId(Integer.toString(resultSet.getInt("account_id")));
					singleServiceRun.setRunBy(Integer.toString(resultSet.getInt("run_by")));
					singleServiceRun.setServiceId(Integer.toString(resultSet.getInt("service_id")));
					singleServiceRun.setPercentCompleted(new BigDecimal(resultSet.getInt("percent_complete")));
					singleServiceRun.setStartDate(resultSet.getDate("start_date").toString());
					if (resultSet.getDate("stop_date") != null) {
						singleServiceRun.setStopDate(resultSet.getDate("stop_date").toString());
					}

					String parameterQuery = "SELECT * FROM service_interface_parameter WHERE interface_id = ?";
					PreparedStatement preparedStatementParameter = DBConnector.prepareStatement(parameterQuery);
					preparedStatementParameter.setInt(1, resultSet.getInt("interface_id"));
					preparedStatementParameter.execute();
					ResultSet resultSetParameter = preparedStatementParameter.getResultSet();

					while (resultSetParameter.next()) {
						final DomeModelParam parameter = new DomeModelParam();
						parameter.setName(resultSetParameter.getString("name"));
						parameter.setType(resultSetParameter.getString("type"));
						parameter.setUnit(resultSetParameter.getString("unit"));
						parameter.setCategory(resultSetParameter.getString("category"));
						parameter.setValue(resultSetParameter.getString("default_value"));
						parameter.setParameterid(resultSetParameter.getString("parameter_id_txt"));
						parameter.setInstancename(resultSetParameter.getString("instancename"));

						if (resultSetParameter.getBoolean("input_parameter")) {
							inputParams.put(resultSetParameter.getString("name"), parameter);
						} else {
							outputParams.put(resultSetParameter.getString("name"), parameter);
						}
					}

					modelInterface.setInParams(inputParams);
					modelInterface.setOutParams(outputParams);
					singleServiceRun.setInterface(modelInterface);

					String projectQuery = "SELECT groups.group_id, groups.group_name FROM service INNER JOIN groups ON service.project_id = groups.group_id WHERE service.service_id = ?";
					PreparedStatement preparedStatementProject = DBConnector.prepareStatement(projectQuery);
					preparedStatementProject.setInt(1, resultSet.getInt("service_id"));
					preparedStatementProject.execute();
					ResultSet resultSetProject = preparedStatementProject.getResultSet();

					if (resultSetProject.next()) {
						simplifiedProject.setId(Integer.toString(resultSetProject.getInt("group_id")));
						simplifiedProject.setTitle(resultSetProject.getString("group_name"));
					}

					singleServiceRun.setProject(simplifiedProject);

					serviceRuns.add(singleServiceRun);
				}
			}

		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(LOGTAG, se.getMessage());
			}

		}

		return serviceRuns;
	}

	public void cancelServiceRun(String id) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		// GetServiceRun retObj = new GetServiceRun();

		try {
			connection.setAutoCommit(false);

			String serviceRunQuery = "UPDATE service_run SET status = 2 WHERE run_id = ?";

			int i = 1;
			PreparedStatement preparedStatement = DBConnector.prepareStatement(serviceRunQuery);
			// if (serviceRunObj.getStatus() != null) {
			// 	preparedStatement.setInt(i, serviceRunObj.getStatus().intValue());
			// 	i++;
			// }
			// if (serviceRunObj.getPercentCompleted() != null) {
			// 	preparedStatement.setInt(i, serviceRunObj.getPercentCompleted().intValue());
			// 	i++;
			// }
			preparedStatement.setInt(i, Integer.parseInt(id));
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to update serviceRun ");
			}

			// retObj = getSingleServiceRun(id);

		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(LOGTAG, se.getMessage());
			}

		}

		// return retObj;
	}

}
