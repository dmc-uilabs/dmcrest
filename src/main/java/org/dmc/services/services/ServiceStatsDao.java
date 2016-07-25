package org.dmc.services.services;

/*
 * This is a dummy DAO class to come out with service statistics.
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

import org.json.JSONException;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;

public class ServiceStatsDao {

	private final String logTag = ServiceStatsDao.class.getName();
	private ResultSet resultSet;
	private Connection connection;


	public ArrayList<ServiceStats> getServiceStats(String serviceId) {

		ArrayList<ServiceStats> list =new ArrayList<ServiceStats>();
		// Now making a dummy ServiceStats item
		ServiceStats ss = new ServiceStats();
		ss.setServiceId(serviceId);
		ServiceStatsItem ssi = new ServiceStatsItem();
		ssi.setAll(new BigDecimal(0.0));
		ssi.setToday(new BigDecimal(0.0));
		ssi.setWeek(new BigDecimal(0.0));
		ss.setSuccessRate(ssi);
		ss.setSuccessfulRuns(ssi);
		ss.setIncompleteRuns(ssi);
		ss.setUnavailableRuns(ssi);
		ss.setRunsByUsers(ssi);
		ss.setUniqueUsers(ssi);
		ss.setAverageTime(ssi);
		list.add(ss);
		return list;
	}//END GET
	
} //END DAO class
