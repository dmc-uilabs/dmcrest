package org.dmc.services.notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.OrganizationUserService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.Notification;
import org.dmc.services.data.entities.Notification.NotificationType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.NotificationRepository;
import org.dmc.services.exceptions.InvalidOrganizationUserException;
import org.dmc.services.projects.ProjectDao;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

	@Inject
	private NotificationRepository notificationRepository;

	@Inject
	private UserService userService;

	@Inject
	private OrganizationUserService organizationUserService;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private ProjectDao projectDao;

	@Transactional
	public void markAllNotificationsReadForUser(Integer userId) {
		notificationRepository.markAllNotificationsReadForUser(userId);
	}

	@Transactional
	public void markNotificationRead(Integer userId, Integer notificationId){
		notificationRepository.markNotificationRead(userId, notificationId);
	}

	public void notifyOrgAdminsOfNewUser(Integer organizationId, String username) {
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		User user = userMapper.mapToEntity(userService.findByUsername(username));
		notifyOrgAdminsOfNewUser(organizationId, user);
	}

	public void notifyOrgAdminsOfNewUser(Integer organizationId, User user) {
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		List<UserModel> orgAdmins = userService.findByOrganizationIdAndRole(organizationId, SecurityRoles.ADMIN);
		List<Notification> notifications = new ArrayList<Notification>();

		for (UserModel admin : orgAdmins) {
			Notification notification = new Notification();
			notification.setType(NotificationType.NEW_USER_JOINED_ORGANIZATION);
			notification.setMessage("Requested to join your organization.");
			notification.setCreatedBy(user);
			notification.setCreatedFor(userMapper.mapToEntity(admin));
			notifications.add(notification);
		}

		notificationRepository.save(notifications);
	}

	public void sendRequestForVerification(Integer userId) throws InvalidOrganizationUserException {
		OrganizationUserModel orgUser = organizationUserService.getByUserId(userId);
		if (orgUser == null) {
			throw new InvalidOrganizationUserException("User has no organization");
		} else if (orgUser.getIsVerified()) {
			throw new InvalidOrganizationUserException("User is already verified");
		}

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		User user = userMapper.mapToEntity(userService.findOne(userId));

		sendRequestForVerification(orgUser.getOrganizationId(), user);
	}

	public void sendRequestForVerification(Integer organizationId, User user) {
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		List<UserModel> orgAdmins = userService.findByOrganizationIdAndRole(organizationId, SecurityRoles.ADMIN);
		List<Notification> notifications = new ArrayList<Notification>();

		for (UserModel admin : orgAdmins) {
			Notification notification = new Notification();
			notification.setType(NotificationType.USER_REQUESTS_VERIFICATION);
			notification.setMessage("Requested to be verified.");
			notification.setCreatedBy(user);
			notification.setCreatedFor(userMapper.mapToEntity(admin));
			notifications.add(notification);
		}

		notificationRepository.save(notifications);
	}

	public void createForSharedDocument(User sender, User recipient, String documentUrl) {

		Notification notification = new Notification();
		notification.setType(NotificationType.DOCUMENT_SHARED);
		notification.setMessage(documentUrl);
		notification.setCreatedBy(sender);
		notification.setCreatedFor(recipient);

		notificationRepository.save(notification);
	}

	public void notifyInviteToWorkspace(User sender, User recipient, String projectId){
		Notification notification = new Notification();
		notification.setType(NotificationType.INVITATION_TO_WORKSPACE);
		notification.setMessage("project.php#/preview/"+projectId);
		notification.setCreatedBy(sender);
		notification.setCreatedFor(recipient);

		notificationRepository.save(notification);
	}
}
