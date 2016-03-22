package org.dmc.services.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.DBConnector;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import javax.xml.ws.http.HTTPException;

public class CompanySkillDao {

	private final String logTag = CompanySkillDao.class.getName();
	private ResultSet resultSet;
	
	// Only declare here and instantiate in method where it is used
	// Instantiating here may cause NullPointer Exceptions
	private Connection connection;
	
    public ArrayList<CompanySkill> getCompanySkills(String userEPPN, int companyID) throws HTTPException {
        ArrayList<CompanySkill> skills = new ArrayList<CompanySkill>();        
        ServiceLogger.log(this.logTag, "User: " + userEPPN + " asking for all skills of the company: " + companyID);
        try {
        	String query = "select * from organization_skill where organization_id = ?";
        	PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, companyID);
			this.resultSet = preparedStatement.executeQuery();
			

            while (this.resultSet.next()) {
            	int id = this.resultSet.getInt("id");
            	String skill = this.resultSet.getString("skill");
                CompanySkill s = new CompanySkill(id,companyID,skill);
                skills.add(s);
            }
        } catch (SQLException e) {
            ServiceLogger.log(this.logTag, e.getMessage());
            throw new HTTPException(HttpStatus.FORBIDDEN.value());  // ToDo: what error should this be?
        }
        return skills;
	}
}