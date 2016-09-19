package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends BaseRepository<Notification, Integer> {

	@Query("update Notification n set n.unread = 'false' where n.createdFor = :userId")
	void markAllNotificationsReadForUser(@Param("userId") Integer userId);

}
