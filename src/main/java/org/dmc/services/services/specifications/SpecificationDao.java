package org.dmc.services.services.specifications;

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
import org.dmc.services.services.RunStats;
import org.dmc.services.services.ServiceSpecialSpecifications;
import org.dmc.services.services.ServiceSpecifications;
import org.dmc.services.services.UsageStats;
import org.dmc.services.sharedattributes.Util;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpecificationDao {

	private final String logTag = SpecificationDao.class.getName();
	private ResultSet resultSet; 

	public SpecificationDao() {}

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
						new TypeReference<ArrayList<CompanyVideo>>() {});
				UsageStats usageStats = mapper.readValue(this.resultSet.getString("UsageStats"), UsageStats.class);
				RunStats runStats = mapper.readValue(this.resultSet.getString("RunStats"), RunStats.class);

				ServiceSpecifications spec = new ServiceSpecifications();
				spec.setId(String.valueOf(this.resultSet.getInt("id")));
				spec.setServiceId(String.valueOf(this.resultSet.getInt("service_id")));
				spec.setInput(this.resultSet.getObject("input", Integer.class));
				spec.setOutput(this.resultSet.getObject("output", Integer.class));
				spec.setSpecial(special);
				spec.setUsageStats(usageStats);
				spec.setRunStats(runStats);
				
				specs.add(spec);
			}
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}
		return specs;
	}
	
	public Specification getSpecification(int serviceID) {
		//ServiceLogger.log(logTag, "In specification search with service id " + serviceID);
		Trial trial = null;
		int numMembers = 0, numProjects = 0, numSuccesses = 0, numFails = 0;
		Usage usage = null;
		int num_inputs = 0, num_outputs = 0;
		String description = "";
		
		String query = "SELECT (SELECT count(owner_id) FROM cem_runnables_running WHERE interface_id = "
				+ serviceID + ") AS membersCount, " + 
				"(SELECT count(group_id) AS count FROM service_subscriptions WHERE interface_id = "
				+ serviceID + ") AS projectsCount, " +
				"d.interface_data FROM dome_interfaces d WHERE d.interface_id = "
					+ serviceID;
		
		
		try {
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				numMembers = resultSet.getInt("membersCount");
				//ServiceLogger.log(logTag, "Number of members - " + numMembers);
				numProjects = resultSet.getInt("projectsCount");
				//ServiceLogger.log(logTag, "Number of projects using service - " + numProjects);
				description = "dummy description; need to get description from service table";//resultSet.getString("description");
				//ServiceLogger.log(logTag, "Description: " + description);
				
				try {
					JSONObject json = new JSONObject(resultSet.getString("interface_data")); 
					
					if(json.has("inParams")){
						JSONObject inputParams = json.getJSONObject("inParams");
						num_inputs = inputParams.length();
					}
					
					if(json.has("outParams")){
						JSONObject outputParams = json.getJSONObject("outParams");
						num_outputs = outputParams.length();
					}
				} catch (JSONException e) {
					ServiceLogger.log(logTag, e.getMessage());
				}
				
				//ServiceLogger.log(logTag, "Number of inputs for service - " + num_inputs);
				//ServiceLogger.log(logTag, "Number of outputs for service - " + num_outputs);
			}
			
			usage = new Usage(numMembers, numProjects);
			//ServiceLogger.log(logTag, "" + usage);
			
			
			resultSet = DBConnector.executeQuery("SELECT count(*) AS Fails FROM runnable_runtimes WHERE interface_id = "
					+ serviceID + " AND date_completed = 0");
			while (resultSet.next()) {
				numFails = resultSet.getInt("Fails");
				//ServiceLogger.log(logTag, "Number of failed tests: " + numFails);	
			}
			
			resultSet = DBConnector.executeQuery("SELECT count(*) AS Successes FROM runnable_runtimes WHERE interface_id = "
					+ serviceID + " AND date_completed != 0");
			while (resultSet.next()) {
				numSuccesses = resultSet.getInt("Successes");
				//ServiceLogger.log(logTag, "Number of passed tests: " + numSuccesses);	
			}
			
			trial = new Trial(numSuccesses, numFails);
			
			return new Specification(description, num_inputs, num_outputs, usage, trial);
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

}