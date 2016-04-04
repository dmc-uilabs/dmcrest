package org.dmc.services.sharedattributes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.ServiceLogger;


public class Util {

	private final String logTag = Util.class.getName();
    private static final Util instance = new Util();

    public static Util getInstance(){
        return instance;
    }

    private Util(){}
    
	/**
	 * 
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
	public int getGeneratedKey(PreparedStatement statement, String column) throws SQLException {
    	ResultSet keys = statement.getGeneratedKeys();
		ServiceLogger.log(logTag, "keys count = " + keys.getMetaData().getColumnCount());
        if (keys.next()) {

            return keys.getInt(column);
        }
        else {
            throw new SQLException("Could not retrieve generated key: " + column);
        }
	}
}