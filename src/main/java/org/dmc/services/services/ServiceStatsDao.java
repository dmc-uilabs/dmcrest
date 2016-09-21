package org.dmc.services.services;

/*
 * This is a dummy DAO class to come out with service statistics.
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.ResultSet;

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
