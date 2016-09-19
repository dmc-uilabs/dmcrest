package org.dmc.services.notification;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.data.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Inject
	private NotificationRepository notificationRepository;
	
	@Transactional
	public void markAllNotificationsReadForUser(Integer userId) {
		notificationRepository.markAllNotificationsReadForUser(userId);
	}
	
}
