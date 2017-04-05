package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends BaseRepository<Notification, Integer> {

	@Modifying
	@Query("update Notification n set n.unread = 'false' where n.createdFor.id = :userId")
	void markAllNotificationsReadForUser(@Param("userId") Integer userId);

	@Modifying
	@Query("update Notification n set n.unread = 'false' where n.createdFor.id = :userId and n.id = :notificationId")
	void markNotificationRead(@Param("userId") Integer userId, @Param("notificationId") Integer notificationId);

}
