package org.dmc.services.notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.UserService;
import org.dmc.services.data.entities.Notification;
import org.dmc.services.data.entities.Notification.NotificationType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.NotificationRepository;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Inject
	private NotificationRepository notificationRepository;
	
	@Inject
	private UserService userService;
	
	@Inject
	private MapperFactory mapperFactory;
	
	@Transactional
	public void markAllNotificationsReadForUser(Integer userId) {
		notificationRepository.markAllNotificationsReadForUser(userId);
	}
	
	public void notifyOrgAdminsOfNewUser(Integer organizationId, User user) {
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		List<UserModel> orgAdmins = userService.findByOrganizationIdAndRole(organizationId, SecurityRoles.ADMIN);
		List<Notification> notifications = new ArrayList<Notification>();
		
		for (UserModel admin : orgAdmins) {
			Notification notification = new Notification();
			notification.setType(NotificationType.NEW_USER_JOINED_ORGANIZATION);
			notification.setMessage(user.getRealname() + " has requested to join your organization");
			notification.setCreatedBy(user);
			notification.setCreatedFor(userMapper.mapToEntity(admin));
			notifications.add(notification);
		}
		
		notificationRepository.save(notifications);
	}
	
}
