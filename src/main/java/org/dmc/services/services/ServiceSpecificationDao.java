package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyVideo;
import org.dmc.services.sharedattributes.Util;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

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
	public Id createServiceSpecification(int serviceId, ServiceSpecifications spec, String userEPPN) throws DMCServiceException {
		
		PreparedStatement statement;
		Connection connection = DBConnector.connection();
		ObjectMapper mapper = new ObjectMapper();
		Util util = Util.getInstance();
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
					
			ServiceLogger.log(logTag, "SPECIAL JSON: " + special.toString());
			connection.setAutoCommit(false);
			
			query = "INSERT INTO service_specifications (service_id, input, output, special, usage_stats, run_stats)"
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, serviceId);
			statement.setObject(2, spec.getInput(), java.sql.Types.INTEGER);
			statement.setObject(3, spec.getOutput(), java.sql.Types.INTEGER);
			statement.setString(4, special);
			statement.setString(5, usageStats);
			statement.setString(6, runStats);
			statement.executeUpdate();
			
			specId = util.getGeneratedKey(statement, "id");
			ServiceLogger.log(logTag, "Created Service Specification: " + specId);
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
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
            ServiceLogger.log(logTag, e.getMessage());
            if (connection != null) {
                try {
                    ServiceLogger.log(logTag, "Transaction createServiceSpecification Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
            }
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