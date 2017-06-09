package org.dmc.services.services;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

import org.json.JSONObject;

public class InputPositionsDAO {

	public int postPositions(PostServiceInputPosition inputPositions)
			throws DMCServiceException {
		String name = "";
		int serviceId = -999;
		try {

			serviceId = new Integer(inputPositions.getServiceId());
			int interfaceId = getInterfaceId(serviceId);
			List<ServiceInputPosition> positions = inputPositions
					.getPositions();
			for (int i = 0; i < positions.size(); i++) {
				ServiceInputPosition position = positions.get(i);
				name = position.getName();
				int pos = (position.getPosition()).intValue();

				String query = "update service_interface_parameter set parameter_position=?"
						+ " where interface_id=? and name=?";
				PreparedStatement preparedStatement = DBConnector
						.prepareStatement(query);
				int p = position.getPosition().intValue();
				preparedStatement.setInt(1, p);
				preparedStatement.setInt(2, interfaceId);
				name = position.getName();
				preparedStatement.setString(3, name);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError,
					"Cannot update service_interface_parameter, serviceId: "
							+ serviceId + " parameterName: " + name);
		}
		return serviceId;
	}

	public int deletePosition(int positionInputId)  throws DMCServiceException
	{
		int result=0;
		try {
			String query = "update service_interface_parameter set parameter_position=-999"
					+ " where parameter_id=?";
			PreparedStatement preparedStatement = DBConnector
					.prepareStatement(query);
			preparedStatement.setInt(1, positionInputId);
			result=preparedStatement.executeUpdate();
		}
		catch (SQLException e){
			throw new DMCServiceException(DMCError.OtherSQLError,
				"Cannot delete service_interface_parameter, parameter_id=: "
						+ positionInputId);
		}
		return result;
	}
	
	public List<ServiceInputsPositions> getPositions(int serviceId) throws DMCServiceException
	{
		List<ServiceInputsPositions> result = new ArrayList<ServiceInputsPositions>();
		ServiceInputsPositions r1 = new ServiceInputsPositions();
		r1.setServiceId(""+serviceId);
		int interfaceId = -999;
		List<ServiceInputPosition> positions = new ArrayList<ServiceInputPosition>();
		
		try{
			interfaceId = getInterfaceId(serviceId);
			String query = "select name,parameter_position from service_interface_parameter"
					+ " where interface_id=?";
			PreparedStatement preparedStatement = DBConnector
					.prepareStatement(query);
			preparedStatement.setInt(1, interfaceId);
			ResultSet r = preparedStatement.executeQuery();
			
			while (r.next())
			{
				ServiceInputPosition position = new ServiceInputPosition();
				position.setName(r.getString("name"));
				position.setPosition(new BigDecimal(r.getInt("parameter_position")));
				positions.add(position);
			}
		}
		catch (SQLException e){
			throw new DMCServiceException(DMCError.OtherSQLError,
				"Cannot get service_interface_parameter, service_id=: "
						+ serviceId);
		}
		r1.setPositions(positions);
		result.add(r1);
		return result;
	}
	public int patchPosition(int interface_id, List<ServiceInputPosition> positions) throws DMCServiceException
	{
		int result=0;
		String name="";
		int position=-999;
		try{
		for (int i=0;i<positions.size();i++)
		{
			ServiceInputPosition inputPosition=positions.get(i);
			name=inputPosition.getName();
			position=inputPosition.getPosition().intValue();
			String query = "update service_interface_parameter set parameter_position=?"
					+ " where name=? and interface_id=?";
			PreparedStatement preparedStatement = DBConnector
					.prepareStatement(query);
			preparedStatement.setInt(1, position);
			preparedStatement.setString(2,name);
			preparedStatement.setInt(3,interface_id);
			result=preparedStatement.executeUpdate();
		}
		}
		catch (SQLException e)
		{
			throw new DMCServiceException(DMCError.OtherSQLError,
					"Cannot patch service_interface_parameter, interface_id=: "
							+ interface_id + ", name = " + name);
		}
		return result;
	}
	
	public int getInterfaceId(int serviceId) throws SQLException {
		int result = -9;

		String query = "select interface_id from service_interface INNER JOIN service ON (service_interface.service_id = service.service_id AND service.project_id != 0) where service_interface.service_id = ?";
		PreparedStatement preparedStatement = DBConnector
				.prepareStatement(query);
		preparedStatement.setInt(1, serviceId);
		ResultSet r = preparedStatement.executeQuery();
		r.next();
		result = r.getInt("interface_id");
		return result;
	}

}
