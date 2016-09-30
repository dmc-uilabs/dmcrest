package org.dmc.services.notification;

import javax.inject.Inject;

import org.dmc.services.exceptions.InvalidOrganizationUserException;
import org.dmc.services.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

	@Inject
	private NotificationService notificationService;
	
	@RequestMapping(value = "/notifications", params = "action=requestVerification", method = RequestMethod.POST)
	public void sendRequestVerificationNotifications() throws InvalidOrganizationUserException {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		notificationService.sendRequestForVerification(user.getId());
	}
	
}
