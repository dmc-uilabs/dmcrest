package org.dmc.services.services;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by 200005921 on 5/12/2016.
 */
public class ServiceTagsDao {

    private final String logTag = ServiceTagsDao.class.getName();
    private ResultSet resultSet;

    public ArrayList<ServiceTag> getServiceList(String userEPPN) throws SQLException, NumberFormatException {
        ArrayList<ServiceTag> tags = new ArrayList<ServiceTag>();
        try {


            resultSet = DBConnector.executeQuery("SELECT * FROM service_tag");

            while (resultSet.next()) {

                String idStr = Integer.toString( resultSet.getInt("id"));
                String serviceIdStr = Integer.toString(resultSet.getInt("service_id"));

                ServiceTag serviceTag = new ServiceTag();
                serviceTag.setId(idStr);
                serviceTag.setServiceId(serviceIdStr);
                serviceTag.setName(resultSet.getString("name"));
                tags.add(serviceTag);
            }
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, nfe.getMessage());
            throw nfe;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw e;
        }
        return tags;
    }

    public ArrayList<ServiceTag> getServiceListById(int id, String userEPPN) throws SQLException, NumberFormatException {
        ArrayList<ServiceTag> tags = new ArrayList<ServiceTag>();
        try {

            resultSet = DBConnector.executeQuery("SELECT * FROM service_tag WHERE id = " + id);

            while (resultSet.next()) {

                String idStr = Integer.toString( resultSet.getInt("id"));
                String serviceIdStr = Integer.toString(resultSet.getInt("service_id"));

                ServiceTag serviceTag = new ServiceTag();
                serviceTag.setId(idStr);
                serviceTag.setServiceId(serviceIdStr);
                serviceTag.setName(resultSet.getString("name"));
                tags.add(serviceTag);
            }

        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, nfe.getMessage());
            throw nfe;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw e;
        }
        return tags;
    }

    public ArrayList<ServiceTag> getServiceListByServiceId(int id, String userEPPN) throws SQLException, NumberFormatException{
        ArrayList<ServiceTag> tags = new ArrayList<ServiceTag>();
        try {

            resultSet = DBConnector.executeQuery("SELECT * FROM service_tag WHERE service_id = " + id);

            while (resultSet.next()) {

                String idStr = Integer.toString( resultSet.getInt("id"));
                String serviceIdStr = Integer.toString(resultSet.getInt("service_id"));

                ServiceTag serviceTag = new ServiceTag();
                serviceTag.setId(idStr);
                serviceTag.setServiceId(serviceIdStr);
                serviceTag.setName(resultSet.getString("name"));
                tags.add(serviceTag);
            }
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, nfe.getMessage());
            throw nfe;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw e;
        }
        return tags;
    }

    public Id insertServiceTag (ServiceTag payload, String userEPPN) throws SQLException, JSONException, Exception {

        Connection connection = DBConnector.connection();

        // let's start a transaction
        connection.setAutoCommit(false);

        try {
            int serviceTagId = -1;

            // create new project in groups table
            String insertServiceTagQuery = "insert into service_tag(service_id, name) values ( ?, ? )";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(insertServiceTagQuery);
            preparedStatement.setInt(1, Integer.parseInt(payload.getServiceId()));
            preparedStatement.setString(2, payload.getName());

            int rCreate = preparedStatement.executeUpdate();

            String queryId = "select max(id) max_id from service_tag";
            PreparedStatement preparedStatement1 = DBConnector
                    .prepareStatement(queryId);
            ResultSet r=preparedStatement1.executeQuery();
            r.next();
            serviceTagId=r.getInt("max_id");
            ServiceLogger.log(logTag,  "inserted ServiceTag id=: " + serviceTagId);

            return new Id.IdBuilder(serviceTagId).build();
        } catch (SQLException ex) {
            ServiceLogger.log(logTag,  "got SQLException in insertServiceTag: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Service Tag Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (JSONException ex) {
            ServiceLogger.log(logTag,  "got JSONException in insertServiceTag: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Service Tag Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (Exception ex) {
            ServiceLogger.log(logTag,  "got Exception in insertServiceTag: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Service Tag Rolled back");
                connection.rollback();
            }
            throw ex;
        } finally {
            // let's end the transaction
            if (null != connection) {
                connection.setAutoCommit(true);
            }
        }

    }

    public boolean deleteServiceTag (int id, String userEPPN) throws SQLException, Exception {

        String deleteServiceTagQuery = "delete from service_tag where id = ? ";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteServiceTagQuery);
        preparedStatement.setInt(1, id);
        int count = preparedStatement.executeUpdate();

        if (count != 1) return false;
        else return true;
    }

}
