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
 * Created by 200005921 on 5/13/2016.
 */
public class SharedServicesDao {

    private final String logTag = SharedServicesDao.class.getName();
    private ResultSet resultSet;

    public ArrayList<SharedService> getSharedServices(String userEPPN) throws SQLException, NumberFormatException {
        ArrayList<SharedService> services = new ArrayList<SharedService>();
        try {


            resultSet = DBConnector.executeQuery("SELECT shared_service.* FROM shared_service INNER JOIN service ON (shared_service.service_id = service.service_id AND service.project_id != 0)");

            while (resultSet.next()) {

                String idStr = Integer.toString( resultSet.getInt("id"));
                String serviceIdStr = Integer.toString(resultSet.getInt("service_id"));

                SharedService sharedService = new SharedService();
                sharedService.setId(Integer.toString(resultSet.getInt("id")));
                sharedService.setServiceId(Integer.toString(resultSet.getInt("service_id")));
                sharedService.setAccountId(Integer.toString(resultSet.getInt("account_id")));
                sharedService.setProfileId(Integer.toString(resultSet.getInt("profile_id")));
                services.add(sharedService);
            }
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, nfe.getMessage());
            throw nfe;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw e;
        }
        return services;
    }

    public ArrayList<SharedService> getSharedServicesById(int id, String userEPPN) throws SQLException, NumberFormatException {
        ArrayList<SharedService> services = new ArrayList<SharedService>();
        try {


            resultSet = DBConnector.executeQuery("SELECT shared_service.* FROM shared_service INNER JOIN service ON (shared_service.service_id = service.service_id AND service.project_id != 0) WHERE id = " + id);

            while (resultSet.next()) {

                String idStr = Integer.toString( resultSet.getInt("id"));
                String serviceIdStr = Integer.toString(resultSet.getInt("service_id"));

                SharedService sharedService = new SharedService();
                sharedService.setId(Integer.toString(resultSet.getInt("id")));
                sharedService.setServiceId(Integer.toString(resultSet.getInt("service_id")));
                sharedService.setAccountId(Integer.toString(resultSet.getInt("account_id")));
                sharedService.setProfileId(Integer.toString(resultSet.getInt("profile_id")));
                services.add(sharedService);
            }
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, nfe.getMessage());
            throw nfe;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw e;
        }
        return services;
    }

    public Id insertSharedService (PostSharedService payload, String userEPPN) throws SQLException, JSONException, Exception {
        Connection connection = DBConnector.connection();

        try {

            // let's start a transaction
            connection.setAutoCommit(false);
            int sharedServiceId = -1;

            // insert new shared_service
            String insertServiceTagQuery = "insert into shared_service(account_id, profile_id, service_id) values ( ?, ?, ? )";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(insertServiceTagQuery);
            preparedStatement.setInt(1, Integer.parseInt(payload.getAccountId()));
            preparedStatement.setInt(2, Integer.parseInt(payload.getProfileId()));
            preparedStatement.setInt(3, Integer.parseInt(payload.getServiceId()));

            int rCreate = preparedStatement.executeUpdate();

            String queryId = "select max(id) max_id from shared_service INNER JOIN service ON (shared_service.service_id = service.service_id AND service.project_id != 0)";
            PreparedStatement preparedStatement1 = DBConnector
                    .prepareStatement(queryId);
            ResultSet r=preparedStatement1.executeQuery();
            r.next();
            sharedServiceId=r.getInt("max_id");
            ServiceLogger.log(logTag,  "inserted shared_service id=: " + sharedServiceId);

            return new Id.IdBuilder(sharedServiceId).build();

        } catch (SQLException ex) {
            ServiceLogger.log(logTag,  "got SQLException in insertSharedService: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Shared Service Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (JSONException ex) {
            ServiceLogger.log(logTag,  "got JSONException in insertSharedService: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Shared Service Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (Exception ex) {
            ServiceLogger.log(logTag,  "got Exception in insertSharedService: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Insert Shared Service Rolled back");
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

}
