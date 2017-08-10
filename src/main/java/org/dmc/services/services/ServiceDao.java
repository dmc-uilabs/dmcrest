package org.dmc.services.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.SqlTypeConverterUtility;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.search.SearchException;
import org.dmc.services.search.SearchQueueImpl;
import org.dmc.services.services.ServiceHistory.PeriodEnum;
import org.dmc.services.services.ServiceHistory.SectionEnum;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.solr.SolrUtils;

import org.dmc.services.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.dmc.services.security.SecurityRoles;

public class ServiceDao {

    private final String logTag = ServiceDao.class.getName();
    private Connection connection = null;

    public Service getService(int requestId, String userEPPN) throws DMCServiceException {

        try {

            final String query = "SELECT * FROM service WHERE service_id = " + requestId + " AND project_id != 0";
            Service service = null;
            final ResultSet resultSet = DBConnector.executeQuery(query);

            if (resultSet.next()) {
                        service = readServiceResultSet(resultSet);
                return service;
            }
            return null;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError,
                    "unable to GET service " + requestId + ": " + e.getMessage());
        }
    }

    public Service createService(Service requestedBody, String userEPPN) throws DMCServiceException {
        try {
            connection = DBConnector.connection();
            // let's start a transaction
            connection.setAutoCommit(false);

            // look up userID
            final int userID = UserDao.getUserID(userEPPN);
            final int companyId = CompanyDao.getUserCompanyId(userID);

            String query = "insert into service (organization_id, title, description, owner_id, release_date, service_type, specifications, project_id, from_location, type, parent, published)";
            query += "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, companyId);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3, requestedBody.getDescription());
            preparedStatement.setInt(4, userID);
            preparedStatement.setObject(5, SqlTypeConverterUtility.getSqlDate(requestedBody.getReleaseDate()),
                    java.sql.Types.DATE);
            preparedStatement.setString(6, requestedBody.getServiceType());
            preparedStatement.setString(7, requestedBody.getSpecifications());
            preparedStatement.setObject(8, SqlTypeConverterUtility.getInt(requestedBody.getProjectId()),
                    java.sql.Types.INTEGER);
            preparedStatement.setString(9, requestedBody.getFrom());
            preparedStatement.setString(10, requestedBody.getType());
            preparedStatement.setString(11, requestedBody.getParent());
            preparedStatement.executeUpdate();

            query = "select currval('service_service_id_seq') as id";
            final ResultSet resultSet = DBConnector.executeQuery(query);

            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            Service service = getService(id, userEPPN);
            connection.commit();

            if (Config.IS_TEST == null) {
                //ServiceLogger.log(LOGTAG, "SolR indexing turned off");
                // Trigger solr indexing
                try {
                    SearchQueueImpl.sendFullIndexingMessage(SolrUtils.CORE_GFORGE_SERVICES);
                    ServiceLogger.log(logTag, "SolR indexing triggered for service (create): " + id);
                } catch (SearchException e) {
                }
            }

            return service;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (null != connection) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + ex.getMessage());
                }
            }
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to create new service: " + e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

    public Service patchService(String serviceIdText, Service requestedBody, String userEPPN)
            throws DMCServiceException {

              // if (!userIsAuthorizedToUpdate(serviceIdText, userEPPN, requestedBody)) {
              //   throw new DMCServiceException(DMCError.NotAuthorizedToChange, "User: " + userEPPN + " is not allowed to update service: " + serviceIdText);
              // }

            try {

            final int serviceId = Integer.parseInt(serviceIdText);
            if (serviceId != requestedBody.getId()) {
                throw new DMCServiceException(DMCError.OtherSQLError,
                        "serviceId " + serviceId + " does not match " + requestedBody.getId() + " as expected");
            }
            connection = DBConnector.connection();
            // let's start a transaction
            connection.setAutoCommit(false);

            // look up userID
            final int userID = UserDao.getUserID(userEPPN);

            String query = "update service set ";
            query += "organization_id=?, ";
            query += "title=?, ";
            query += "description=?, ";
            query += "release_date=?, ";
            query += "service_type=?, ";
            query += "project_id=?, ";
            query += "from_location=?, ";
            query += "type=?, ";
            query += "parent=?, ";
            query += "published=?, ";
            query += "support=? ";
            query += "where ";
            query += "service_id=?";
            // removing the below to allow all superAdmins to modify services
            // query += "owner_id=?";

            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setObject(1, SqlTypeConverterUtility.getInt(requestedBody.getCompanyId()),
                    java.sql.Types.INTEGER);
            preparedStatement.setString(2, requestedBody.getTitle());
            preparedStatement.setString(3, requestedBody.getDescription());
            preparedStatement.setObject(4, SqlTypeConverterUtility.getSqlDate(requestedBody.getReleaseDate()),
                    java.sql.Types.DATE);
            preparedStatement.setString(5, requestedBody.getServiceType());
            preparedStatement.setObject(6, SqlTypeConverterUtility.getInt(requestedBody.getProjectId()),
                    java.sql.Types.INTEGER);
            preparedStatement.setString(7, requestedBody.getFrom());
            preparedStatement.setString(8, requestedBody.getType());
            preparedStatement.setString(9, requestedBody.getParent());
            preparedStatement.setBoolean(10, requestedBody.getPublished());
            preparedStatement.setString(11,requestedBody.getSupport());
            preparedStatement.setInt(12, serviceId);
            // preparedStatement.setInt(12, userID);
            final int rowsAffected = preparedStatement.executeUpdate();
            if (1 != rowsAffected) {
                throw new Exception("didn't correctly modify service " + requestedBody.getId());
            }
            final Service service = getService(requestedBody.getId(), userEPPN);

            final String updateServiceHistoryQuery = "INSERT INTO service_history (service_id, title, date, user_id, link, section, period)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";
            final PreparedStatement historyUpdate = DBConnector.prepareStatement(updateServiceHistoryQuery);
            final Date now = new Date();
            historyUpdate.setInt(1, serviceId);
            historyUpdate.setString(2, UserDao.getUserName(userID) + " updated the service on " + now.toString());
            historyUpdate.setObject(3, SqlTypeConverterUtility.getSqlDate(now), java.sql.Types.DATE);
            historyUpdate.setInt(4, userID);
            historyUpdate.setString(5, "");
            historyUpdate.setString(6, "marketplace");
            historyUpdate.setString(7, now.toString());
            final int historyAffected = historyUpdate.executeUpdate();

            if (historyAffected != 1)
                throw new DMCServiceException(DMCError.UnableToLogServiceHistory, "Could not log service history!");

            connection.commit();

            if (Config.IS_TEST == null) {
                //ServiceLogger.log(LOGTAG, "SolR indexing turned off");
                // Trigger solr indexing
                try {
                    SearchQueueImpl.sendFullIndexingMessage(SolrUtils.CORE_GFORGE_SERVICES);
                    ServiceLogger.log(logTag, "SolR indexing triggered for service (update): " + serviceId);
                } catch (SearchException e) {
                }
            }
            return service;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            if (null != connection) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DMCServiceException(DMCError.OtherSQLError, "unable to rollback: " + ex.getMessage());
                }
            }
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to create new service: " + e.getMessage());
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

    public ArrayList<Service> getServiceList() throws DMCServiceException {
        final ArrayList<Service> list = new ArrayList<Service>();
        ResultSet resultSet = null;
        try {

            resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id != 0");

            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }

            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get serviceList: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

    // accounting for ?id=?&id=?... passed to endpoint
    public ArrayList<Service> getServiceListByIds(List<Integer> serviceIds) throws DMCServiceException {
    	ArrayList<Service> returnList = new ArrayList<Service>();
    	ResultSet resultSet = null;
    	try {
    		String query = "SELECT * FROM service WHERE project_id != 0";

    		for(int i = 0; i < serviceIds.size(); i++) {
    			if(i == 0) {
    				query += " AND (service_id = " + serviceIds.get(i);
    			} else {
    				query += " OR service_id = " + serviceIds.get(i);
    			}
    		}
        if(serviceIds.size() > 0){
          query += ")";
        }

    		resultSet = DBConnector.executeQuery(query);

    		while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                returnList.add(service);
            }

    		return returnList;
    	} catch(Exception e) {
    		ServiceLogger.log(logTag,  e.getMessage());
    		throw new DMCServiceException(DMCError.OtherSQLError, "unable to get serviceList: " + e.getMessage());
    	} finally {
    		if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
    	}
    }

    public ArrayList<Service> getServiceList(int projectId) throws DMCServiceException {
        final ArrayList<Service> list = new ArrayList<Service>();
        ResultSet resultSet = null;
        try {

            resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + projectId);

            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }

            return list;

        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError,
                    "unable to get services for project " + projectId + ": " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

    public ArrayList<Service> getServiceByComponentList(int componentId) throws DMCServiceException {
        final ArrayList<Service> list = new ArrayList<Service>();
        ResultSet resultSet = null;
        try {
            // ToDo need to determine component ID
            resultSet = DBConnector.executeQuery("SELECT * FROM service WHERE project_id = " + componentId);

            while (resultSet.next()) {
                Service service = readServiceResultSet(resultSet);
                list.add(service);
            }

            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError,
                    "unable to get services by component: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }

	public ArrayList<Service> getServices(Integer limit,
            String order,
            Integer start,
            String sort,
            String titleLike,
            String serviceType,
            List<Integer> authors,
            List<String>ratings,
            String favorites,
            List<String> dates,
            String published,
            List<String> fromLocations,
            String userEPPN) {
		return getServices(limit, order, start, sort, titleLike, serviceType, authors, ratings, favorites, dates, published, fromLocations, userEPPN, null);
	}

    public ArrayList<Service> getServices(Integer limit,
            String order,
            Integer start,
            String sort,
            String titleLike,
            String serviceType,
            List<Integer> authors,
            List<String>ratings,
            String favorites,
            List<String> dates,
            String published,
            List<String> fromLocations,
            String userEPPN,
            Integer filterByCompany)
                throws DMCServiceException {
        final ArrayList<Service> list=new ArrayList<Service>();
        ResultSet resultSet = null;
        try {
            final PreparedStatement preparedStatement = setupGetServicesQuery(limit, order, start, sort, titleLike,
                                                                              serviceType, authors, ratings, favorites, dates, published,
                                                                              fromLocations, userEPPN, filterByCompany);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final Service service = readServiceResultSet(resultSet);
                list.add(service);
            }

            return list;
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to get services: " + e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }
    }


    private PreparedStatement setupGetServicesQuery(Integer limit,
			String order,
			Integer start,
			String sort,
			String titleLike,
			String serviceType,
			List<Integer> authors,
			List<String>ratings,
			String favorites,
			List<String> dates,
			String published,
			List<String> fromLocations,
			String userEPPN,
			Integer filterByCompany)
                throws Exception {
        String query = "SELECT * FROM service";

        final ArrayList<String> whereClauses = new ArrayList<String>();
        final ArrayList<String> orderByClauses = new ArrayList<String>();

        whereClauses.add(" project_id != 0");

        if (null != fromLocations && fromLocations.size() > 0) {
            String fromClause = " from_location in (?";
            // already have first placeholder, so start count from 1 instead of
            // 0
            for (int i = 1; i < fromLocations.size(); ++i) {
                fromClause += ", ?";
            }
            fromClause += ")";
            whereClauses.add(fromClause);
        }


        if(null != filterByCompany) {
            String fromClause = " organization_id = ?";
            whereClauses.add(fromClause);
        }

        if (null != dates && dates.size() > 0) {
            String datesClause = " ( ";
            for (String dateItem : dates) {
                datesClause += " release_date > now() - INTERVAL ";
                char intervalType = dateItem.charAt(dateItem.length() - 1);
                Integer dateCount = Integer.parseInt(dateItem.substring(0, dateItem.length() - 1));
                String convertedIntervalType = convertIntervalType(intervalType);
                datesClause += "'" + dateCount + convertedIntervalType + "'";
                datesClause += " OR ";
            }
            if (datesClause.endsWith(" OR ")) {
                datesClause = datesClause.substring(0, datesClause.length() - 4);
            }
            datesClause += ")";
            whereClauses.add(datesClause);
        }

		if(published != null && (published.equals("true") || published.equals("false"))) {
			String publishedClause = " published = ?";
			whereClauses.add(publishedClause);
		}

        query += addClauses(whereClauses, " WHERE ", " AND ");
        query += addClauses(orderByClauses, " ORDER BY ", ", ");

        ServiceLogger.log(logTag, "query: " + query);

        final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
        int parameterIndex = 1;
        for (String location : fromLocations) {
            preparedStatement.setString(parameterIndex, location);
            ServiceLogger.log(logTag, "  parameter " + parameterIndex + " : from " + location);
            parameterIndex++;
        }

        if(null != filterByCompany) {
            preparedStatement.setInt(parameterIndex, filterByCompany);
            parameterIndex++;
        }

        if(published != null && (published.equals("true") || published.equals("false"))) {
			preparedStatement.setBoolean(parameterIndex, Boolean.parseBoolean(published));
			parameterIndex++;
		}

        if (null != start) {
        }
        return preparedStatement;
    }

    private Service readServiceResultSet(ResultSet resultSet) throws SQLException {
        final Service service = new Service();
        service.setId(resultSet.getInt("service_id"));
        service.setCompanyId(Integer.toString(resultSet.getInt("organization_id")));
        if (resultSet.wasNull())
            service.setCompanyId(null);
        service.setTitle(resultSet.getString("title"));
        service.setDescription(resultSet.getString("description"));
        service.setOwner(resultSet.getString("owner_id"));
        service.setProfileId(resultSet.getString("owner_id")); // ToDo: up date
        service.setReleaseDate(resultSet.getDate("release_date"));
        if (resultSet.wasNull())
            service.setReleaseDate(null);
        service.setServiceType(resultSet.getString("service_type"));
        service.setSupport(resultSet.getString("support"));
        service.setType(resultSet.getString("service_type"));
        service.setTags(new ArrayList<String>()); // ToDo: up date
        service.setSpecifications(resultSet.getString("specifications"));

        service.setFeatureImage(new FeatureImage("", ""));
        service.setCurrentStatus(new ServiceCurrentStatus(0, "", ""));

        service.setProjectId(Integer.toString(resultSet.getInt("project_id")));
        if (resultSet.wasNull()) {
            service.setProjectId(null);
        }
        service.setFrom(resultSet.getString("from_location"));
        service.setType(resultSet.getString("type"));
        service.setParent(resultSet.getString("parent"));
        service.setPublished(resultSet.getBoolean("published"));


        service.setAverageRun("");
        return service;
    }

    private String addClauses(ArrayList<String> clauses, String start, String connector) {
        String queryClause = "";
        if (clauses.size() > 0) {
            queryClause += start;
            for (String clause : clauses) {
                queryClause += clause;
                queryClause += connector;
            }
            // remove any trailing AND
            if (queryClause.endsWith(connector)) {
                queryClause = queryClause.substring(0, queryClause.length() - connector.length());
            }
        }
        return queryClause;
    }

    String convertIntervalType(char intervalType) {
        String convertedIntervalType = "";
        switch (intervalType) {
        case 'd':
            convertedIntervalType = " day ";
            break;
        case 'm':
            convertedIntervalType = " month ";
            break;
        case 'y':
            convertedIntervalType = " year ";
            break;
        default:
            convertedIntervalType = " day ";
            break;
        }
        return convertedIntervalType;
    }

    public List<ServiceHistory> getHistory(String serviceIdText, String period, String section, String userEPPN) {

        try {
            Integer serviceId = Integer.parseInt(serviceIdText);
            final String permissionsQuery1 = "SELECT published, project_id FROM service WHERE service_id = ? AND project_id != 0";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(permissionsQuery1);
            preparedStatement.setInt(1, serviceId);
            ResultSet rs = preparedStatement.executeQuery();
            boolean published = false;
            int projectID = -1;
            while (rs.next()) {
                published = rs.getBoolean("published");
                projectID = rs.getInt("project_id");
            }

            if (!published) {
                final int uid = UserDao.getUserID(userEPPN);
                final String permissionsQuery2 = "SELECT * FROM group_join_request WHERE group_id = ? AND user_id = ? "
                        + "AND accept_date IS NOT NULL";

                final PreparedStatement ps = DBConnector.prepareStatement(permissionsQuery2);
                ps.setInt(1, projectID);
                ps.setInt(2, uid);

                rs = ps.executeQuery();

                if (!rs.next()) {
                    throw new DMCServiceException(DMCError.MemberNotAssignedToProject,
                            "You are not allowed to view this service");
                }

            }

            final StringBuilder serviceHistoryQuery = new StringBuilder("SELECT * FROM service_history WHERE service_id = ?");
            if (period != null)
                serviceHistoryQuery.append(" AND period = ?");

            if (section != null)
                serviceHistoryQuery.append(" AND section = ?");

            final PreparedStatement ps = DBConnector.prepareStatement(serviceHistoryQuery.toString());
            ps.setInt(1, serviceId);
            if (period != null)
                ps.setString(2, period);

            if (section != null)
                ps.setString(3, section);
            final ResultSet resSet = ps.executeQuery();

            final ArrayList<ServiceHistory> historyList = new ArrayList<ServiceHistory>();

            while (resSet.next()) {
                final ServiceHistory history = new ServiceHistory();
                history.setId(Integer.toString(resSet.getInt("id")));
                history.setLink(resSet.getString("link"));
                final SectionEnum sectionVal = resSet.getString("section").toLowerCase().equals("project")
                        ? SectionEnum.project : SectionEnum.marketplace;
                history.setSection(sectionVal);
                history.setServiceId(Integer.toString(resSet.getInt("service_id")));
                history.setTitle(resSet.getString("title"));
                final CharSequence logged = resSet.getString("date");
                history.setDate(logged.toString());
                history.setUser(resSet.getString("user_id"));

                final String dateFormat = "yyyy-MM-dd";
                final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
                final LocalDate loggedDate = LocalDate.parse(logged, dtf);

                final LocalDate now = LocalDate.now();

                final Duration dur = Duration.between(loggedDate.atStartOfDay(), now.atStartOfDay());

                PeriodEnum pd;
                if (dur.toDays() >= 365)
                    pd = PeriodEnum.year;
                else if (dur.toDays() >= 30) // calculate based on start month
                                             // soon enough
                    pd = PeriodEnum.month;
                else if (dur.toDays() >= 1)
                    pd = PeriodEnum.week;
                else
                    pd = PeriodEnum.today;

                history.setPeriod(pd);

                historyList.add(history);

                ServiceLogger.log(logTag, "found history for serviceId: " + serviceId + "\n" + history.toString());

            }
            return historyList;

        } catch (SQLException e) {
            ServiceLogger.log(logTag, "getHistory error logging: " + e.getMessage());
            throw new DMCServiceException(DMCError.UnknownSQLError, e.getMessage());
        }

    }

    public Boolean userIsAuthorizedToUpdate(String serviceId, String userEPPN, Service requestedBody) {
      Boolean isAuthorized = false;

      try {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Service service = getService(Integer.parseInt(serviceId), userEPPN);

        if (user.hasAuthority(SecurityRoles.SUPERADMIN)) {
          isAuthorized = true;
        } else if (Integer.parseInt(service.getOwner()) == user.getId() && !requestedBody.getPublished()) {
          isAuthorized = true;
        }

      } catch (Exception e) {
        System.out.println(e);
      }

      return isAuthorized;
    }

}
