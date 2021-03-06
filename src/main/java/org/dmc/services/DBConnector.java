package org.dmc.services;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnector {
	
	private final static String logTag = DBConnector.class.getName();

	private static DBConnector connectorInstance = null;
	private Statement stmt = null;
	private Connection conn = null;
    private final String url = Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME;

	private static JdbcTemplate jdbcTemplate;

	protected DBConnector() {
		
		// Register the postgress driver
		// Note: WEB-INF/lib/postgresql-9.4-1201-jdbc41.jar must be copied to $CATALINA_HOME/lib/ 
		// after deploy for driver to be available
		try  {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		
		// Connect
    	try {
	    	Properties props = new Properties();
	    	props.setProperty("user", Config.DB_USER);
	    	props.setProperty("password", Config.DB_PASS);
	    	
	    	conn = DriverManager.getConnection(url, props);
	    	
	    	this.stmt = conn.createStatement();
	    	ServiceLogger.log(logTag, "Connection URL - " + url);
    	} catch (SQLException e) {
    		ServiceLogger.log(logTag, "Message: " + e.getMessage());
			ServiceLogger.log(logTag, "SQL State exception: " + e.getSQLState());
            ServiceLogger.log(logTag, "SQL Error Code: "+ e.getErrorCode());
    	}

		jdbcTemplate = createJdbcTemplate();
	}

	public static JdbcTemplate jdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate = createJdbcTemplate();
		}
		return jdbcTemplate;
	}

	public static ResultSet executeQuery(String query) {
		try {
			if (connectorInstance == null || connectorInstance.stmt.isClosed()) {
				connectorInstance = new DBConnector();
			}
			return connectorInstance.stmt.executeQuery(query);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	public static Connection connection() {
		try {
			if (connectorInstance == null || !connectorInstance.conn.isValid(0)) {
				connectorInstance = new DBConnector();
			}
			return connectorInstance.conn;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

	//
	// Use PreparedStatement to reduce chance of SQL Injection attacks and frequently improve performance
	// http://stackoverflow.com/questions/3271249/difference-between-statement-and-preparedstatement
	// 
	// Use set methods to bind the parameters in the query with values (e.g. setString, setTimestamp, setInt, etc.)
	// Then use executeUpdate (for data manipulation statements), executeQuery method to run query with bound parameters,
	// or execute for more complex results (batch statements).
	//
	public static PreparedStatement prepareStatement(String query) {
		try {
			return connection().prepareStatement(query);
		} catch(SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}
	
	// Use this prepared statement to return generated keys. 
	// For example to retrieve the serial keys when an insert is performed
	public static PreparedStatement prepareStatement(String query, int returnKeys) {
		try {
			return connection().prepareStatement(query, returnKeys);
		} catch(SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

	private static DataSource createDataSource() {
		PGPoolingDataSource ds = new PGPoolingDataSource();
		ds.setUser(Config.DB_USER);
		ds.setPassword(Config.DB_PASS);
		ds.setServerName(Config.DB_IP);
		ds.setPortNumber(Integer.parseInt(Config.DB_PORT));
		ds.setDatabaseName(Config.DB_NAME);
		return ds;
	}

	private static JdbcTemplate createJdbcTemplate() {
		return new JdbcTemplate(createDataSource());
	}
}