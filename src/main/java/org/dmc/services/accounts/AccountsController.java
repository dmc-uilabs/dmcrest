package org.dmc.services.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.discussions.FollowDiscussionsDao;
import org.dmc.services.discussions.FollowingIndividualDiscussion;
import org.dmc.services.member.FollowingMemberDao;
import org.dmc.services.member.FollowingMember;
import org.dmc.services.products.FavoriteProduct;
import org.dmc.services.products.FavoriteProductsDao;

import javax.xml.ws.http.HTTPException;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/accounts", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-22T14:57:06.776Z")
public class AccountsController {

    private final String logTag = AccountsController.class.getName();
    private AccountsDao accounts = new AccountsDao();

    @RequestMapping(value = "/{accountID}", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<UserAccount> accountsAccountIDGet(@PathVariable("accountID") String accountID,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "accountsAccountIDGet, accountID: " + accountID);

        int httpStatusCode = HttpStatus.OK.value();
        UserAccount userAccount = null;

        try {
            userAccount = accounts.getUserAccount(accountID, userEPPN);
        } catch (HTTPException httpException) {
            httpStatusCode = httpException.getStatusCode();
        }

        return new ResponseEntity<UserAccount>(userAccount, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/{accountID}", produces = { "application/json" }, method = RequestMethod.PATCH)
    public ResponseEntity<UserAccount> accountsAccountIDPatch(@PathVariable("accountID") String accountID,
            @RequestBody UserAccount account,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "accountsAccountIDPatch, accountID: " + accountID + ", account.id: " + account.getId()
                + ", userEPPN: " + userEPPN);

        int httpStatusCode = HttpStatus.OK.value();
        UserAccount userAccount = null;

        try {
            userAccount = accounts.patchUserAccount(accountID, account, userEPPN);
        } catch (HTTPException httpException) {
            httpStatusCode = httpException.getStatusCode();
        }
        return new ResponseEntity<UserAccount>(userAccount, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/{accountID}/account-notification-settings", produces = {
            "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<List<AccountNotificationSetting>> accountsAccountIDAccountNotificationSettingsGet(
            @PathVariable("accountID") String accountID) {
        // do some magic!
        return new ResponseEntity<List<AccountNotificationSetting>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/{accountID}/account_servers", produces = {
            "application/json" }, method = RequestMethod.GET)
    public ResponseEntity accountsAccountIDAccountServersGet(@PathVariable("accountID") String accountID,
            @RequestParam(value = "_limit", required = false) Integer limit,
            @RequestParam(value = "_order", required = false) String order,
            @RequestParam(value = "_sort", required = false) String sort) {

        AccountsDao accountsDao = new AccountsDao();

        try {
            ServiceLogger.log(logTag, "In accountsAccountIDAccountServersGet, accountID = " + accountID);
            return new ResponseEntity<List<UserAccountServer>>(
                    accountsDao.getAccountServersFromAccountID(accountID, limit, order, sort), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

    }

    @RequestMapping(value = "/{accountID}/favorite_products", produces = {
            "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<?> accountsAccountIDFavoriteProductsGet(
            @PathVariable("accountID") String accountID,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {

        ServiceLogger.log(logTag, "In accountsAccountIDFavoriteProductsGet:  as user " + userEPPN);
        FavoriteProductsDao favoriteProductsDao = new FavoriteProductsDao();
        List<Integer> accountIds = new ArrayList<Integer>();
        accountIds.add(Integer.parseInt(accountID));
        
        try {
            return new ResponseEntity<List<FavoriteProduct>>(favoriteProductsDao.getFavoriteProductForAccounts(accountIds, limit, order, sort, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/{accountID}/following_companies", produces = {APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<?> accountsAccountIDFollowingCompaniesGet(
			@PathVariable("accountID") String accountID, 
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "getAccountsFollowingCompanyByAccountsId, userEPPN: " + userEPPN);
		List<FollowingCompany> followingCompanies = null;
		try {
			followingCompanies = accounts.getFollowingCompaniesByAccountId(accountID, limit, order, sort, userEPPN);
			return new ResponseEntity<List<FollowingCompany>>(followingCompanies, HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

    @RequestMapping(value = "/{accountID}/follow_discussions", produces = {
            APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity getFollowDiscussionsFromAccountId(
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
            @PathVariable("accountID") String accountID,
            @RequestParam(value = "individual-discussionId", required = false) String individualDiscussionId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort) {
        final FollowDiscussionsDao followDiscussionsDao = new FollowDiscussionsDao();
        try {
            ServiceLogger.log(logTag, "In getFollowDiscussionsFromAccountId");
            return new ResponseEntity<List<FollowingIndividualDiscussion>>(followDiscussionsDao
                    .getFollowedDiscussionsforAccount(accountID, individualDiscussionId, limit, order, sort, userEPPN),
                    HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/{accountId}/following_members", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<List<FollowingMember>> accountsAccountIdFollowingMembersGet(
            @PathVariable("accountId") String accountId, 
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        final FollowingMemberDao dao = new FollowingMemberDao();
        return new ResponseEntity<List<FollowingMember>>(dao.followingMembersGet(accountId, null, null, limit, start, order, sort, userEPPN), HttpStatus.OK);
    }

}
