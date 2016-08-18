package org.dmc.services.users;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.UserService;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserTokenModel;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.security.UserPrincipalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.xml.ws.http.HTTPException;
import java.util.List;

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
    public Id createUser(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
                         @RequestHeader(value="AJP_givenName", defaultValue="testUserFirstName") String userFirstName,
                         @RequestHeader(value="AJP_sn", defaultValue="testUserSurname") String userSurname,
                         @RequestHeader(value="AJP_displayName", defaultValue="testUserFullName") String userFull,
                         @RequestHeader(value="AJP_mail", defaultValue="testUserEmail") String userEmail)
    {
    	ServiceLogger.log(logTag, "In createUser: " + userEPPN);

    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON

        return userDAO.createUser(userEPPN, userFirstName, userSurname, userFull, userEmail);

    	//Create role and update db through JDBC then return role using new role's id
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
                        @RequestHeader(value="AJP_givenName", defaultValue="testUserFirstName") String userFirstName,
                        @RequestHeader(value="AJP_sn", defaultValue="testUserSurname") String userSurname,
                        @RequestHeader(value="AJP_displayName", defaultValue="testUserFullName") String userFull,
                        @RequestHeader(value="AJP_mail", defaultValue="testUserEmail") String userEmail)
    {
        ServiceLogger.log(logTag, "In user: " + userEPPN);

        User user = userDAO.getUser(userEPPN, userFirstName, userSurname, userFull, userEmail);

		UserModel model = userService.readOrCreateUser(userEPPN, userFirstName, userSurname, userFull, userEmail);

        UserPrincipal userPrincipal = (UserPrincipal) userPrincipalService.loadUserByUsername(userEPPN);
        user.setIsDMDIIMember(userPrincipal.hasAuthority(SecurityRoles.DMDII_MEMBER));
        user.setRoles(userPrincipal.getAllRoles());

        return user;
    }

    @RequestMapping(value = "/user", produces = { "application/json" }, method = RequestMethod.PATCH)
    public ResponseEntity<User> patchUser(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
                                          @RequestBody User patchUser)
    {
        ServiceLogger.log(logTag, "In patchUser: " + userEPPN);

        int httpStatusCode = HttpStatus.OK.value();
        User patchedUser = null;

        try{
            patchedUser = userDAO.patchUser(userEPPN, patchUser);
        } catch(HTTPException httpException) {
            httpStatusCode = httpException.getStatusCode();
        }

        return new ResponseEntity<User>(patchedUser, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserModel getUser(@PathVariable Integer id) {
		return userService.findOne(id);
	}

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
	public UserModel saveUser(@RequestBody UserModel user) {
		return userService.save(user);
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

	@RequestMapping(value = "/user/verify", method = RequestMethod.POST)
	public VerifyUserResponse verifyUser(@RequestParam("userId") Integer id, @RequestParam("token") String token) {
		return userService.verifyUser(id, token);
	}

	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	@RequestMapping(value = "/user/unverify", method = RequestMethod.POST)
	public VerifyUserResponse unverifyUser(@RequestParam("userId") Integer userId) {
		Integer organizationId = orgUserService.getOrganizationUserByUserId(userId).getOrganizationId();
		if(PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationId)) {
			return userService.unverifyUser(userId);
		} else {
			throw new AccessDeniedException("403 Permission Denied");
		}
	}

    /*
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateRole(@RequestParam(value="id", defaultValue="-1") int id) {
    	System.out.println("In createRole role: " + id);


    	//RoleDao.createRole updates the Role in the database identified by id using the provided POST params
    	//it creates an instance of this role i.e new Role(param.id, param.name, param.title.....)
    	//this controller in turn returns this updated Role instance to the reques using spring's Jackson which
    	//converts the response to JSON

    	return RoleDao.updateRole(params);
    }
    */

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
        // prepare responseEntity
        ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
    	ServiceLogger.log(logTag, ex.getMessage() + " Error message " + result);
    	return result;
    }
}
