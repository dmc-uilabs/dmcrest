package org.dmc.services.users;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.UserService;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserTokenModel;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.security.UserPrincipalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final String logTag = UserController.class.getName();

	private UserDao userDAO = new UserDao();

	@Inject
	private UserService userService;

	@Inject
	private OrganizationUserService orgUserService;

	@Inject
	private UserPrincipalService userPrincipalService;

    @RequestMapping(value = "/users/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
	public Id createUser(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @RequestHeader(value="AJP_givenName", defaultValue="testUserFirstName") String userFirstName,
                         @RequestHeader(value="AJP_sn", defaultValue="testUserSurname") String userSurname,
                         @RequestHeader(value="AJP_displayName", defaultValue="testUserFullName") String userFull,
                         @RequestHeader(value="AJP_mail", defaultValue="testUserEmail") String userEmail)
    {
    	ServiceLogger.log(logTag, "In createUser: " + userEPPN);

		return new Id(userService.readOrCreateUser(userEPPN, userFirstName, userSurname, userFull, userEmail)
				.getId());
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
	public UserModel getUser(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @RequestHeader(value="AJP_givenName", defaultValue="testUserFirstName") String userFirstName,
                        @RequestHeader(value="AJP_sn", defaultValue="testUserSurname") String userSurname,
                        @RequestHeader(value="AJP_displayName", defaultValue="testUserFullName") String userFull,
                        @RequestHeader(value="AJP_mail", defaultValue="testUserEmail") String userEmail)
    {
        ServiceLogger.log(logTag, "In user: " + userEPPN);
		return userService.readOrCreateUser(userEPPN, userFirstName, userSurname, userFull, userEmail);
	}

	@RequestMapping(value = "/user", produces = { "application/json" }, method = RequestMethod.PATCH)
	public ResponseEntity<UserModel> patchUser(
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@RequestBody UserModel patchUser) {
		ServiceLogger.log(logTag, "In patchUser: " + userEPPN);

		UserModel userModel = null;
		int httpStatusCode = HttpStatus.OK.value();

		try {
			userModel = userService.patch(userEPPN, patchUser);
		} catch (IllegalArgumentException e) {
			ServiceLogger.log(logTag, e.getMessage());
			httpStatusCode = HttpStatus.BAD_REQUEST.value();
		}
		return new ResponseEntity<UserModel>(userModel, HttpStatus.valueOf(httpStatusCode));
	}

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserModel getUser(@PathVariable Integer id) {
		return userService.findOne(id);
	}

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
	public UserModel saveUser(@RequestBody UserModel user,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		return userService.save(user, userEPPN);
	}

	@RequestMapping(value = "/user/organization/{organizationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserModel> getUsersByOrganizationId(@PathVariable Integer organizationId) {
		return userService.findByOrganizationId(organizationId);
	}

	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	@RequestMapping(value = "/user/createtoken", method = RequestMethod.POST)
	public UserTokenModel saveToken(@RequestParam("userId") Integer userId) {
		Integer organizationId = orgUserService.getOrganizationUserByUserId(userId).getOrganizationId();
		if(PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationId)) {
			return userService.createToken(userId);
		} else {
			throw new AccessDeniedException("403 Permission Denied");
		}
	}

	@RequestMapping(value = "/users/{userId}", params = { "action" }, method = RequestMethod.PUT)
	public VerifyUserResponse userAction(@PathVariable Integer userId, @RequestParam("action") String action,
			@RequestBody(required = false) UserTokenModel token) throws ArgumentNotFoundException {
		UserPrincipal loggedIn = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer organizationId = orgUserService.getOrganizationUserByUserId(userId).getOrganizationId();
		VerifyUserResponse response;

		if ("verify".equals(action)) {

			if (userId.equals(loggedIn.getId())) {
				response = userService.verifyUser(userId, token.getToken());
			} else {
				throw new AccessDeniedException("403 Permission Denied");
			}
		} else if ("unverify".equals(action)) {

			if (PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationId)) {
				response = userService.unverifyUser(userId);
			} else {
				throw new AccessDeniedException("403 Permission Denied");
			}
		} else if ("decline".equals(action)) {

			if (PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationId)) {
				response = userService.declineUser(userId, organizationId);
			} else {
				throw new AccessDeniedException("403 Permission Denied");
			}

		} else {
			response = new VerifyUserResponse(1000, "Unknown action \"" + action + "\" requested.");
		}

		return response;
	}

	@RequestMapping(value = "/users/{userId}/organizations", method = RequestMethod.PUT)
	public OrganizationUserModel changeOrganization(@PathVariable Integer userId, @RequestBody OrganizationUserModel orgUser) {
		if(!userId.equals(orgUser.getUserId())) {
			throw new RuntimeException("User passed in request doesn't match user that's getting updated.");
		}
		return orgUserService.changeOrganization(orgUser);
	}

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
        ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
    	ServiceLogger.log(logTag, ex.getMessage() + " Error message " + result);
    	return result;
    }
}
