package org.dmc.services.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceSpecificationDao {

	private final String logTag = ServiceSpecificationDao.class.getName();
	private ResultSet resultSet; 

	public ServiceSpecificationDao() {}

	/**
	 * Create Service Specification
	 * @param serviceId
	 * @param spec
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ArrayList<Integer> createServiceSpecifications(int serviceId, ArrayList<ServiceSpecifications> specs, String userEPPN) throws DMCServiceException {
		
		PreparedStatement statement;
		Connection connection = DBConnector.connection();
		ObjectMapper mapper = new ObjectMapper();
		Util util = Util.getInstance();
		String query;
		String special, usageStats, runStats;
		ArrayList<Integer> ids = null;
		
		try {

			connection.setAutoCommit(false);
			
			query = "INSERT INTO service_specifications (service_id, input, output, special, usage_stats, run_stats)"
					+ " VALUES (?, ?, ?, ?, ?, ?)";
			
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			for (ServiceSpecifications spec : specs) {
				
				// when using POSt array_secifications, serviceId provided is -1, 
				// the expectation is that the service Id will be contained in the specification object
				serviceId = (serviceId == -1) ? Integer.parseInt(spec.getServiceId()) : 1;
				
				special = mapper.writeValueAsString(spec.getSpecial());
				usageStats = mapper.writeValueAsString(spec.getUsageStats());
				runStats = mapper.writeValueAsString(spec.getRunStats());
				ServiceLogger.log(logTag, "Special JSON: " + special);
				ServiceLogger.log(logTag, "Usage Stats JSON: " + usageStats);
				ServiceLogger.log(logTag, "Run Stats JSON: " + runStats);
				ServiceLogger.log(logTag, "special JSON: " + special.toString());
				statement.setInt(1, serviceId);
				statement.setObject(2, spec.getInput(), java.sql.Types.INTEGER);
				statement.setObject(3, spec.getOutput(), java.sql.Types.INTEGER);
				statement.setString(4, special);
				statement.setString(5, usageStats);
				statement.setString(6, runStats);
				statement.addBatch();
			}
			
			statement.executeBatch();
			connection.commit();
			ids = util.getGeneratedKeys(statement, "id");
			
		} catch (SQLException e) {
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction createServiceSpecification Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {}
			}
		}
		
		return ids;
	}
	
	/**
	 * Update Service Specification
	 * @param serviceId
	 * @param spec
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public Id updateServiceSpecification(int id, ServiceSpecifications spec, String userEPPN) throws DMCServiceException {
		
		PreparedStatement statement;
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();
		ObjectMapper mapper = new ObjectMapper();
		String query;
		String special, usageStats, runStats;
		int specId = -9999;
		
		try {

			special = mapper.writeValueAsString(spec.getSpecial());
			usageStats = mapper.writeValueAsString(spec.getUsageStats());
			runStats = mapper.writeValueAsString(spec.getRunStats());
			ServiceLogger.log(logTag, "Special JSON: " + special);
			ServiceLogger.log(logTag, "Usage Stats JSON: " + usageStats);
			ServiceLogger.log(logTag, "Run Stats JSON: " + runStats);
			
			connection.setAutoCommit(false);
			
			query = "UPDATE service_specifications SET (input = ?, output  = ?, special = ?, usage_stats = ?, run_stats = ?"
					+ "WHERE id = ?";
			
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setObject(1, spec.getInput(), java.sql.Types.INTEGER);
			statement.setObject(2, spec.getOutput(), java.sql.Types.INTEGER);
			statement.setString(3, special);
			statement.setString(4, usageStats);
			statement.setString(5, runStats);
			statement.setInt(6, id);
			statement.executeUpdate();
			
			specId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Created Service Specification: " + specId);
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(logTag, "Transaction updateServiceSpecification Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(logTag, ex.getMessage());
				}
			}
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {}
			}
		}
		
		return new Id.IdBuilder(specId).build();
	}
	
	/**
	 * Get Services Specifications
	 * @param limit
	 * @param order
	 * @param sort
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ArrayList<ServiceSpecifications> getServiceSpecifications(int serviceId, int limit, String order, String sort, String userEPPN) throws DMCServiceException {
		
		ArrayList<ServiceSpecifications> specs = new ArrayList<ServiceSpecifications>();
		ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all service specifications");
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String query = "SELECT * FROM service_specifications"; 
			if (serviceId != -1) {
				query += " WHERE service_id = " + serviceId;
			} else {
				query += " ORDER BY " + sort + " " + order + " LIMIT " + limit;
			}
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			this.resultSet = preparedStatement.executeQuery();
			
			while (this.resultSet.next()) {
				List<ServiceSpecialSpecifications> special = mapper.readValue(this.resultSet.getString("special"), 
						new TypeReference<ArrayList<ServiceSpecialSpecifications>>() {});
				
				UsageStats usageStats = mapper.readValue(this.resultSet.getString("usage_stats"), UsageStats.class);
				RunStats runStats = mapper.readValue(this.resultSet.getString("run_stats"), RunStats.class);

				ServiceSpecifications spec = new ServiceSpecifications();
				spec.setId(String.valueOf(this.resultSet.getInt("id")));
				spec.setServiceId(String.valueOf(this.resultSet.getInt("service_id")));
				spec.setInput(this.resultSet.getInt("input"));
				spec.setOutput(this.resultSet.getInt("output"));
				spec.setSpecial(special);
				spec.setUsageStats(usageStats);
				spec.setRunStats(runStats);
				
				specs.add(spec);
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}
		return specs;
	}
	
}