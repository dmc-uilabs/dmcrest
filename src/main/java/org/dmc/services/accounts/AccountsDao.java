package org.dmc.services.accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.company.CompanyDao;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;

class AccountsDao {
    
    private final String logTag = AccountsDao.class.getName();
    
    /**
     
     **/
    public UserAccount getUserAccount(String user_id_string, String userEPPN) throws HTTPException {
        int user_id = Integer.parseInt(user_id_string);
        int user_id_lookedup = -1;
        
        try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        if(user_id_lookedup != user_id) {
            throw new HTTPException(HttpStatus.FORBIDDEN.value()); // user id for userEPPN does not match user_id_string, return default UserAccount
        }
        
        UserAccount userAccount = new UserAccount();
		CompanyDao companyDao = new CompanyDao();
		int companyId = companyDao.getUserCompanyId(user_id);
        
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            
            ServiceLogger.log(logTag, "getUserAccount, user_id: " + user_id + ", userEPPN: " + userEPPN);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // get results
                userAccount.setId(Integer.toString(user_id)); //set in constructor
                userAccount.setCompanyId(Integer.toString(companyId));
                userAccount.setProfileId(Integer.toString(user_id));
                
                userAccount.setDisplayName(resultSet.getString("realname"));
                userAccount.setFirstName(resultSet.getString("firstname"));
                userAccount.setLastName(resultSet.getString("lastname"));
                userAccount.setEmail(resultSet.getString("email"));
                userAccount.setDeactivated(resultSet.getBoolean("status"));
                userAccount.setLocation(resultSet.getString("address2"));
                userAccount.setTimezone(resultSet.getString("timezone"));
                userAccount.setPrivacy(new UserAccountPrivacy());
            }
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        return userAccount;
    }
    
    
    /**
     
     **/
    public UserAccount patchUserAccount(String user_id_string, UserAccount account, String userEPPN) throws HTTPException {
        int user_id_lookedup = -1;
        int account_user_id = Integer.parseInt(account.getId());
        int user_id_passedOnPath = Integer.parseInt(user_id_string);
        
        ServiceLogger.log(logTag, "In patchUserAccount, finding account id = " + account_user_id + ", userEPPN: " + userEPPN);
        
        try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        // check if user has permission to update account
        if(user_id_lookedup != account_user_id || user_id_lookedup != user_id_passedOnPath || account_user_id != user_id_passedOnPath) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
        }
        
        // the parameter account contains the full UserAccount object for the calling user
        ServiceLogger.log(logTag, "In patchUserAccount, patching user_id = " + user_id_string + ", userEPPN: " + userEPPN);
        
        try {
            // update user's record in users table
            String createAccountQuery = "UPDATE users SET realname = ?, firstname = ?, lastname = ?, email = ?, address2 = ?, timezone = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(createAccountQuery);
            preparedStatement.setString(1, account.getDisplayName());
            preparedStatement.setString(2, account.getFirstName());
            preparedStatement.setString(3, account.getLastName());
            preparedStatement.setString(4, account.getEmail());
            // do not update status, this is an admin task
            preparedStatement.setString(5, account.getLocation());
            preparedStatement.setString(6, account.getTimezone());     // need to add time zone
            // need to create db table to store UserAccountPrivacy
            
            preparedStatement.setInt(7, Integer.parseInt(user_id_string)); // set user_id in WHERE clause
            
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
        return account;
    }
    
	public List<UserAccountServer> getAccountServersFromAccountID(String accountID, Integer limit, String order, String sort) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		UserAccountServer retObj = null;
		List<UserAccountServer> userAccountServers = new ArrayList<UserAccountServer>();

		try {
			connection.setAutoCommit(false);

			ArrayList<String> columnsInServersTable = new ArrayList<String>();
			columnsInServersTable.add("server_id");
			columnsInServersTable.add("url");
			columnsInServersTable.add("user_id");
			columnsInServersTable.add("alias");
			columnsInServersTable.add("status");

			String domeInterfacesQuery = "SELECT server_id, url, user_id, alias, status FROM servers WHERE user_id = ?";

			if (sort == null) {
				domeInterfacesQuery += " ORDER BY server_id";
			} else if (!columnsInServersTable.contains(sort)) {
				domeInterfacesQuery += " ORDER BY server_id";
			} else {
				domeInterfacesQuery += " ORDER BY " + sort;
			}

			if (order == null) {
				domeInterfacesQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				domeInterfacesQuery += " ASC";
			} else {
				domeInterfacesQuery += " " + order;
			}

			if (limit == null) {
				domeInterfacesQuery += " LIMIT ALL";
			} else if (limit < 0) {
				domeInterfacesQuery += " LIMIT 0";
			} else {
				domeInterfacesQuery += " LIMIT " + limit;
			}

			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, new Integer(Integer.parseInt(accountID)));
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			while (resultSet.next()) {
				retObj = new UserAccountServer();

				retObj.setId(Integer.toString(resultSet.getInt("server_id")));
				retObj.setIp(resultSet.getString("url"));
				retObj.setAccountId(Integer.toString(resultSet.getInt("user_id")));
				retObj.setName(resultSet.getString("alias"));
				retObj.setStatus(resultSet.getString("status"));

				userAccountServers.add(retObj);
			}

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

		return userAccountServers;
	}
	
	
	public List<FollowingCompany> getFollowingCompaniesByAccountId(String accountID, Integer limit, String order,
			String sort, String userEPPN) throws DMCServiceException {
		ServiceLogger.log(logTag, "Start running getFollowingCompaniesByAccountId: ");
		Connection connection = DBConnector.connection();
		FollowingCompany followingCompany = null;
		List<FollowingCompany> followingCompanies = new ArrayList<FollowingCompany>();
		int userId = -1;
		try {
			userId = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, e.getMessage());
		}	
		if(userId != Integer.parseInt(accountID)){
			ServiceLogger.log(logTag, "Current user is not authorized user!");
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, "Current user is not authorized user!");
			
		}
		try {
			connection.setAutoCommit(false);
			final ArrayList<String> columnsInUserCompanyFollowTable = new ArrayList<String>();
			columnsInUserCompanyFollowTable.add("id");
			columnsInUserCompanyFollowTable.add("account_id");
			columnsInUserCompanyFollowTable.add("company_id");
			
			String query = "SELECT * from user_company_follow WHERE account_id = " + Integer.parseInt(accountID);
			if (sort == null) {
				query += " ORDER BY id";
			} else if (!columnsInUserCompanyFollowTable.contains(sort)) {
				query += " ORDER BY id";
			} else {
				query += " ORDER BY " + sort;
			}

			if (order == null) {
				query += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				query += " ASC";
			} else {
				query += " " + order;
			}

			if (limit == null) {
				query += " LIMIT ALL";
			} else if (limit < 0) {
				query += " LIMIT 0";
			} else {
				query += " LIMIT " + limit;
			}
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();
			while (resultSet.next()) {
				followingCompany = new FollowingCompany();
				followingCompany.setId(resultSet.getString("id"));
				followingCompany.setCompanyId(resultSet.getString("company_id"));
				followingCompany.setAccountId(resultSet.getString("account_id"));
				followingCompanies.add(followingCompany);
			}
			return followingCompanies;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				ServiceLogger.log(logTag, e1.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, "Unable to roll back " + e1.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError,
					"Unable to get following companies " + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
    
}