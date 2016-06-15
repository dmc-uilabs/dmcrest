package org.dmc.services.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.User;
import org.dmc.services.users.UserDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import static org.dmc.services.company.CompanyUserUtil.isMemberOfCompany;

public class EventsDao 
{

	private final String logTag = EventsDao.class.getName();
	private ResultSet resultSet;
	
	// Only declare here and instantiate in method where it is used
	// Instantiating here may cause NullPointer Exceptions
	private Connection connection;
	
    public ArrayList<CommunityEvent> getEvents() throws HTTPException {
        ArrayList<CommunityEvent> events = null;
        ServiceLogger.log(logTag, "In Get Events");
        
        CommunityEvent event = new CommunityEvent();
		event.setId("a");
		event.setTitle("b");
		event.setDate("c");
		event.setStartTime("d");
		event.setEndTime("e");;
		event.setAddress("f");
		event.setDescription("g");
        events.add(event);
/*
			//For GET Event
	        try {
            // get events;
            // does the organization need to be active member?  assume no.
            resultSet = DBConnector.executeQuery("SELECT organization_id, accountid, name FROM organization");
            companies = new ArrayList<Company>();

			//filling id, title, date, startTime, endTime, address, description
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getInt("title");
                String date = resultSet.getString("date");
				String startTime = resultSet.getString("startTime");
				String  endTime = resultSet.getString("endTime");
				String  address = resultSet.getString("address");
				String  description= resultSet.getString("description");
				
				//DOES THIS NEED SOMETHING LIKE A NEW COMPANY/LIKE  NEW EVENT?
                Company company = new Company();
				company.setId(Integer.toString(id));
				company.setAccountId(Integer.toString(accountId));
				company.setName(name);
                companies.add(company);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return companies;
	}
			//For POST Event
			//For PATCH Event
			//For DELETE Event
 */

      

        
        
        /*		
 * 
 * 
        try {
            // get all organizations;
            // does the organization need to be active member?  assume no.
            resultSet = DBConnector.executeQuery("SELECT organization_id, accountid, name FROM organization");
            events = new ArrayList<CommunityEvent>();

            while (resultSet.next()) {
                int id = resultSet.getInt("organization_id");
                int accountId = resultSet.getInt("accountid");
                String name = resultSet.getString("name");

                Event event = new event();
				event.setId(Integer.toString(id));
				event.setAccountId(Integer.toString(accountId));
				event.setName(name);
                events.add(event);
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        */
        return events;
	}
}



