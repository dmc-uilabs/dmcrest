package org.dmc.services.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.dao.user.UserOnboardingDao;
import org.dmc.services.services.GetCompareService;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.utils.SQLUtils;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class ProfileDao {

    private static final String LOGTAG = ProfileDao.class.getName();

	public ArrayList<Profile> getProfiles(String userEPPN, Integer limit, String order, String sort, List<String> id)
			throws DMCServiceException {

        String whereClause = "";
        if (null != id) {
            ServiceLogger.log(LOGTAG, "getProfiles intList equal " + id.toString());

            String commaDelimitedIdList = StringUtils.collectionToDelimitedString(id, ",");
            whereClause = "WHERE user_id IN (" + commaDelimitedIdList + ")";
        }

        ArrayList<Profile> profiles = new ArrayList<Profile>();

        try {
            final String query = "SELECT user_id, user_name, realname, title, phone, email, address, image, people_resume FROM users "
                    + whereClause + " ORDER BY " + sort + " " + order + " LIMIT ?";

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            ServiceLogger.log(LOGTAG, preparedStatement.toString());

            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();

            while (rs.next()) {
                Profile profile = new Profile();
                profile = setProfileValues(rs);
                profiles.add(profile);
            }
        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }

        return profiles;
    }

    private Profile setProfileValues(ResultSet resultSet) throws SQLException {
        Profile profile = new Profile();
        String user_id_string = resultSet.getString("user_id");
        int user_id = Integer.parseInt(user_id_string);

        // get company
        final CompanyDao companyDao = new CompanyDao();
        final int companyId = companyDao.getUserCompanyId(user_id);
        profile.setCompany(Integer.toString(companyId));

        profile.setId(user_id_string);
        profile.setDisplayName(resultSet.getString("realname"));
        profile.setJobTitle(resultSet.getString("title"));
        profile.setPhone(resultSet.getString("phone"));
        profile.setEmail(resultSet.getString("email"));
        profile.setLocation(resultSet.getString("address"));
        profile.setImage(resultSet.getString("image"));
        profile.setDescription(resultSet.getString("people_resume"));

        // need to get skills;
        ArrayList<String> skills = getSkills(profile.getId());
        profile.setSkills(skills);

        return profile;
    }

    public Profile getProfile(int requestId) throws HTTPException {
        ServiceLogger.log(LOGTAG, "In getProfile: user_id " + requestId);
        Profile profile = new Profile();
        profile.setId(Integer.toString(requestId));

        try {
            final String queryProfile = "SELECT user_id, user_name, realname, title, phone, email, address, image, people_resume FROM users WHERE user_id = ?";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(queryProfile);
            preparedStatement.setInt(1, requestId);

            final ResultSet resultSet = preparedStatement.executeQuery();
            String userName = null;

            if (resultSet.next()) {
                profile = setProfileValues(resultSet);
                userName = resultSet.getString("user_name");
            }

            int user_id_lookedup = -1;
            try {
                user_id_lookedup = UserDao.getUserID(userName);
            } catch (SQLException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
            }

            // check user name is equal to userEPPN
            if (user_id_lookedup != requestId) {
                throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
            }

        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            throw new HTTPException(HttpStatus.GATEWAY_TIMEOUT.value());
        }
        return profile;
    }

    public Id createProfile(Profile profile, String userEPPN) throws DMCServiceException {
        int userId = -99999;
        try {
            userId = UserDao.getUserID(userEPPN);
        } catch (SQLException se) {
            ServiceLogger.log(LOGTAG, se.getMessage());
        }

        try {
            updateProfile(userId, profile, userEPPN);
        } catch (DMCServiceException e) {
            throw e;
        }
        return new Id.IdBuilder(userId).build();
    }

    public Id updateProfile(int id, Profile profile, String userEPPN) throws HTTPException, DMCServiceException {
        PreparedStatement statement;
        String query;

        final Connection connection = DBConnector.connection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            return null;
        }

        try {

            // update user
            query = "UPDATE users SET "
                    + "realname = ?, title = ?, phone = ?, email = ?, address = ?, image = ?, people_resume = ? "
                    + "WHERE user_id = ? AND user_name = ?";

            statement = DBConnector.prepareStatement(query);
            statement.setString(1, profile.getDisplayName());
            statement.setString(2, profile.getJobTitle());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getEmail());
            statement.setString(5, profile.getLocation());
            statement.setString(6, profile.getImage());
            statement.setString(7, profile.getDescription());
            statement.setInt(8, id);
            statement.setString(9, userEPPN);
            // @TODO set company
            statement.executeUpdate();

            // update onboarding status
            final UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
            userOnboardingDao.setProfile(id, true);

            if (Config.IS_TEST == null) {
                // SolrUtils.invokeFulIndexingUsers();
                ServiceLogger.log(LOGTAG, "SolR indexing triggered for User: " + id);
            }

            // delete and create skills
            if (this.deleteSkills(id) && profile.getSkills().size() > 0) {
                this.createSkills(id, profile.getSkills());
            }
            connection.commit();
        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            if (connection != null) {
                try {
                    ServiceLogger.log(LOGTAG, "Transaction Profile Update Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(LOGTAG, ex.getMessage());
                }
            }
            throw new HTTPException(HttpStatus.GATEWAY_TIMEOUT.value());
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    // don't really need to do anything
                }
            }
        }


        return new Id.IdBuilder(id).build();
    }

    public Id deleteProfile(int id, String userEPPN) {
        PreparedStatement statement;

        final Connection connection = DBConnector.connection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            return null;
        }

        try {
            // update onboarding status
            final UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
            userOnboardingDao.deleteUserOnboarding(id);

			this.deleteSkills(id);
			final String query = "DELETE FROM users WHERE user_id = ? AND user_name = ?";
			statement = DBConnector.prepareStatement(query);
			statement.setInt(1, id);
			statement.setString(2, userEPPN);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(LOGTAG, "Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(LOGTAG, ex.getMessage());
				}
			}
			return null;
		} catch (JSONException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			if (connection != null) {
				try {
					ServiceLogger.log(LOGTAG, "Transaction Profile Delete Rolled back");
					connection.rollback();
				} catch (SQLException ex) {
					ServiceLogger.log(LOGTAG, ex.getMessage());
				}
			}
			return null;
		} finally {
			if (null != connection) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException ex) {
					// don't really need to do anything
				}
			}
		}
		return new Id.IdBuilder(id).build();
	}

    public boolean createSkills(int userId, ArrayList<String> skills) throws SQLException {
        final Util util = Util.getInstance();
        PreparedStatement statement;
        int skillId;

        if (skills.size() != 0) {
            for (int i = 0; i < skills.size(); i++) {
                String query = "INSERT INTO people_skill (name) VALUES (?)";
                statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, skills.get(i));
                statement.executeUpdate();
                skillId = util.getGeneratedKey(statement, "skill_id");
                ServiceLogger.log(LOGTAG, "PEOPLE SKILL ID: " + skillId);

                query = "INSERT INTO people_skill_inventory (user_id, skill_id) VALUES (?, ?) ";
                statement = DBConnector.prepareStatement(query);
                statement.setInt(1, userId);
                statement.setInt(2, skillId);
                statement.executeUpdate();
            }
        }

        return true;
    }

    public ArrayList<String> getSkills(int userId) throws SQLException {
        ArrayList<String> skills = new ArrayList<String>();

        String query = "SELECT people_skill.name " + "FROM people_skill INNER JOIN people_skill_inventory "
                + "ON people_skill.skill_id=people_skill_inventory.skill_id "
                + "WHERE people_skill_inventory.user_id = ?";

        PreparedStatement statement = DBConnector.prepareStatement(query);
        statement.setInt(1, userId);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String skill = resultSet.getString("name");
            skills.add(skill);
        }

        return skills;
    }

    public boolean deleteSkills(int userId) throws SQLException {
        PreparedStatement statement;
        String query;

        query = "DELETE FROM people_skill WHERE skill_id IN (SELECT skill_id FROM people_skill_inventory WHERE user_id = ?)";
        statement = DBConnector.prepareStatement(query);
        statement.setInt(1, userId);
        statement.executeUpdate();

        query = "DELETE FROM people_skill_inventory WHERE user_id = ?";
        statement = DBConnector.prepareStatement(query);
        statement.setInt(1, userId);
        statement.executeUpdate();

        return true;
    }

    public List<GetCompareService> getCompareServices(String profileID, String userEPPN) throws DMCServiceException {
        int user_id_lookedup = -1;
        String errorHeader = "CHECK AUTHORIZED USER ID : ";
        try {
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e.getMessage());
        }

        // check if user has permission to compare services
        if (user_id_lookedup != Integer.parseInt(profileID)) {
            String errMessage = errorHeader + "current user id " + user_id_lookedup
                    + " does not match id of compare user " + profileID;
            ServiceLogger.log(LOGTAG, errMessage);
            throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errMessage);
            // current id and user id do not match
        }

        List<GetCompareService> compareServices = new ArrayList<GetCompareService>();
        final String query = "SELECT service_compare_id, service_compare.service_id, profile_id FROM service_compare INNER JOIN service ON (service_compare.service_id = service.service_id AND service.project_id != 0) WHERE profile_id = ?";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
        try {
            preparedStatement.setInt(1, Integer.parseInt(profileID));
            preparedStatement.execute();
            ResultSet res = preparedStatement.getResultSet();
            while (res.next()) {
                GetCompareService service = setCompareService(res);
                compareServices.add(service);
            }

        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        } catch (Exception e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            throw new DMCServiceException(DMCError.Generic, e.getMessage());
        }
        return compareServices;
    }

    public GetCompareService setCompareService(ResultSet res) throws SQLException {
        GetCompareService compareService = new GetCompareService();
        String service_compare_id = res.getString("service_compare_id");
        String service_id = res.getString("service_id");
        String profile_id = res.getString("profile_id");

        compareService.setId(service_compare_id);
        compareService.setServiceId(service_id);
        compareService.setProfileId(profile_id);

        return compareService;
    }

    private int checkAuthorizedUser(String profileId, String userEPPN) throws DMCServiceException {
        int user_id_lookedup = -1;
        String errorHeader = "ACCOUNT SERVERS GET AUTHORIZED USER ID: ";
        try {
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e.getMessage()); // unknown
                                                                                               // user
        }
        // check if user has permission to update account
        if (user_id_lookedup != Integer.parseInt(profileId)) {
            String errMessage = errorHeader + "current user id " + user_id_lookedup
                    + " does not match id of compare user " + profileId;
            ServiceLogger.log(LOGTAG, errMessage);
            throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errMessage);
            // current id and user id do not match
        }
        return user_id_lookedup;
    }

    public ArrayList<ProfileHistory> getHistory(String profileId, String section, String order, String sort, Integer limit, Integer start, String userEPPN) throws DMCServiceException {
        try {
            int userid = UserDao.getUserID(userEPPN);
            if (!Integer.toString(userid).equals(profileId)) {
                throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, userEPPN + " not allowed to query history for " + profileId);
            }
            final ArrayList<String> queries = new ArrayList<String>();
            queries.add(getProjectInvitationHistoryQuery(userid));
            queries.add(getProjectJoinHistoryQuery(userid));
            queries.add(getProjectDeclineHistoryQuery(userid));
            queries.add(getProjectDiscussionsHistoryQuery(userid));
            queries.add(getPublicDiscussionsHistoryQuery(userid));
            queries.add(getProjectDiscussionCommentHistoryQuery(userid));
            queries.add(getPublicDiscussionCommentHistoryQuery(userid));
            queries.add(getServiceReleaseHistoryQuery(userid));
            queries.add(getServiceRunStartHistoryQuery(userid));
            queries.add(getServiceRunStopHistoryQuery(userid));
            final ArrayList<ProfileHistory> history = processEventHistoryQuery(queries, order, sort, limit, start);
            return history;
        } catch (SQLException sqle) {
            throw new DMCServiceException(DMCError.OtherSQLError, "unexpected failure getting history " + sqle.getMessage());
        } catch (Exception e) {
            throw new DMCServiceException(DMCError.BadURL, "unexpected failure getting history " + e.getMessage());
        }
    }

    private ArrayList<ProfileHistory> processEventHistoryQuery(ArrayList<String> queries, String order, String sort, Integer limit, Integer start) throws SQLException {
        final ArrayList<ProfileHistory> history = new ArrayList<ProfileHistory>();
        if (queries.size() > 0) {
            String joinQuery = queries.get(0);
            for (int i = 1; i < queries.size(); ++i) {
                joinQuery += " UNION " + queries.get(i);
            }
            //joinQuery += "ORDER BY event_date desc, message ";
            final ArrayList<String> validSortFields = new ArrayList<String>();
            validSortFields.add("event_date");
            validSortFields.add("message");
            
            final String fixedOrder = (null == order & null == sort) ? "DESC" : order;
            final String fixedSort = (null == sort) ? "event_date" : sort.replace("text", "message");
            joinQuery += SQLUtils.buildOrderByClause(fixedOrder, fixedSort, validSortFields);
            joinQuery += SQLUtils.buildLimitClause(limit);
            joinQuery += SQLUtils.buildOffsetClause(start);
            ServiceLogger.log(LOGTAG, "history query: " + joinQuery);
            final PreparedStatement statement = DBConnector.prepareStatement(joinQuery);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final ProfileHistory item = new ProfileHistory();
                final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                Timestamp eventDate = resultSet.getTimestamp("event_date");
                item.setDate(format.format(eventDate));
                item.setTitle(resultSet.getString("message"));
                item.setType(ProfileHistory.mapToType(resultSet.getString("type")));
                item.setLink(resultSet.getString("link"));
                history.add(item);
            }
        }
        return history;
    }
    private String getProjectInvitationHistoryQuery(int userid) {
        final String query = "select 'added' as type, "
                + "gjr.request_date as event_date, "
                + "concat('invited to project ', g.group_name, '(', g.group_id, ') by ', ur.realname) as message, "
                + "concat('project.php#/', g.group_id, '/home') as link "
                + "from groups as g, group_join_request as gjr, users as ur "
                + "where gjr.user_id = " + userid + " "
                + "and g.group_id = gjr.group_id "
                + "and gjr.reject_date is null "
                + "and gjr.accept_date is null "
                + "and gjr.requester_id = ur.user_id "
                + "and g.group_id not in (select group_id from group_join_request "
                + "                       where user_id = " + userid + " and (accept_date is not null or reject_date is not null)) ";
        return query;
    }

    private String getProjectJoinHistoryQuery(int userid) {
        final String query = "select 'added' as type, "
                + "gjr.accept_date as event_date, "
                + "concat('joined project ', g.group_name, '(', g.group_id, ')') as message, "
                + "concat('project.php#/', g.group_id, '/home') as link "
                + "from groups as g, group_join_request as gjr "
                + "where gjr.user_id = " + userid + " "
                + "and g.group_id = gjr.group_id "
                + "and gjr.accept_date is not null ";
        return query;
    }

    private String getProjectDeclineHistoryQuery(int userid) {
        final String query = "select 'completed' as type, "
                + "gjr.reject_date as event_date, "
                + "concat('declined project invitation for ', g.group_name, '(', g.group_id, ')') as message, "
                + "null as link "
                + "from groups as g, group_join_request as gjr "
                + "where gjr.user_id = " + userid + " "
                + "and g.group_id = gjr.group_id "
                + "and gjr.reject_date is not null ";
        return query;
    }

    private String getProjectDiscussionsHistoryQuery(int userid) {
        // currently date is stored as string instead of an actual timestamp
        final String query = "select 'discussion' as type, "
                + "to_timestamp(to_number(d.created_at, '9999999999999999')) as event_date, "
                + "concat('started \"', title, '\" discussion in project ', g.group_name, ' [p', g.group_id, ' d',d.id,']') as message, "
                + "concat('individual-discussion.php#/', d.id) as link "
                + "from individual_discussions d, groups g "
                + "where d.project_id = g.group_id "
                + "and d.account_id = " + userid + " ";
        return query;
    }

    private String getPublicDiscussionsHistoryQuery(int userid) {
        // currently date is stored as string instead of an actual timestamp
        final String query = "select 'discussion' as type, "
                + "to_timestamp(to_number(d.created_at, '99999999999999999999')) as event_date, "
                + "concat('started \"', title, '\" public discussion [d',d.id,']') as message, "
                + "concat('individual-discussion.php#/', d.id) as link "
                + "from individual_discussions d "
                + "where d.project_id is null "
                + "and d.account_id = " + userid + " ";
        return query;
    }

    private String getProjectDiscussionCommentHistoryQuery(int userid) {
        final String query = "select 'discussion' as type, "
                + "to_timestamp(to_number(c.created_at, '99999999999999999999')) as event_date, "
                + "concat('commented on \"', title, '\" ', g.group_name, ' project discussion [p', g.group_id, ' d',d.id, ' c',c.id,']') as message, "
                + "concat('individual-discussion.php#/', d.id) as link "
                + "from individual_discussions d, groups g, individual_discussions_comments c "
                + "where d.project_id = g.group_id "
                + "and c.individual_discussion_id = d.id "
                + "and d.account_id = " + userid + " ";
        return query;
    }

    private String getPublicDiscussionCommentHistoryQuery(int userid) {
        final String query = "select 'discussion' as type, "
                + "to_timestamp(to_number(c.created_at, '99999999999999999999')) as event_date, "
                + "concat('commented on \"', title, '\" public discussion [d',d.id, ' c',c.id,']') as message, "
                + "concat('individual-discussion.php#/', d.id) as link "
                + "from individual_discussions d, individual_discussions_comments c "
                + "where d.project_id is null "
                + "and c.individual_discussion_id = d.id "
                + "and d.account_id = " + userid + " ";
        return query;
    }

    private String getServiceReleaseHistoryQuery(int userid) {
        final String query = "select 'shared' as type, "
                + "s.release_date + time '00:00' as event_date, "
                + "concat('service \"', s.title, '\" released ') as message, "
                + "concat('project.php#/',s.project_id, '/services/', s.service_id,'/detail') as link "
                + "from service s "
                + "where s.release_date is not null "
                + "and s.project_id != 0 "
                + "and owner_id = " + userid + " ";
        return query;
    }

    private String getServiceRunStartHistoryQuery(int userid) {
        final String query = "select 'worked' as type, "
                + "sr.start_date + time '00:00' as event_date, "
                + "concat('service \"', s.title, '\" run started [r',sr.run_id,']') as message, "
                + "concat('project.php#/',s.project_id, '/services/', s.service_id,'/detail') as link "
                + "from service s, service_run sr "
                + "where s.service_id = sr.service_id "
                + "and s.project_id != 0 "
                + "and sr.start_date is not null "
                + "and run_by = " + userid + " ";
        return query;
    }

    private String getServiceRunStopHistoryQuery(int userid) {
        final String query = "select 'completed' as type, "
                + "sr.stop_date + time '00:00' as event_date, "
                + "concat('service \"', s.title, '\" run stopped [r',sr.run_id,']') as message, "
                + "concat('project.php#/',s.project_id, '/services/', s.service_id,'/detail') as link "
                + "from service s, service_run sr "
                + "where s.service_id = sr.service_id "
                + "and s.project_id != 0 "
                + "and sr.stop_date is not null "
                + "and run_by = " + userid + " ";
        return query;
    }
}
