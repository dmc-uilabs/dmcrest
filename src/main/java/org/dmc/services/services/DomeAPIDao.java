package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.sharedattributes.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DomeAPIDao {
	
	private final String logTag = DomeAPIDao.class.getName();
	
	public String getChildren(DomeEntity domeEntity) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		GetDomeInterface retObj = null;
		boolean readSomethingFromTable = false;
		
		try {
			connection.setAutoCommit(false);

				
			
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
		return new String();
	}

}