package org.dmc.services.sharedattributes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		if (keys.next()) {
			return keys.getInt(column);
		} else {
			throw new SQLException("Could not retrieve generated key: " + column);
		}
	}
	
	/**
	 * 
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Integer> getGeneratedKeys(PreparedStatement statement, String column) throws SQLException {
		ArrayList<Integer> keys = new ArrayList<Integer>();

		ResultSet rs = statement.getGeneratedKeys();

		while (rs.next()) {
			keys.add(rs.getInt(column));
		}

		if (keys.isEmpty()) {
			throw new SQLException("Could not retrieve generated keys: " + column);
		}

		return keys;
	}
}