package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Notification;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Modifying;
=======
>>>>>>> Add base for new notification system
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends BaseRepository<Notification, Integer> {

<<<<<<< HEAD
	@Modifying
	@Query("update Notification n set n.unread = 'false' where n.createdFor.id = :userId")
=======
	@Query("update Notification n set n.unread = 'false' where n.createdFor = :userId")
>>>>>>> Add base for new notification system
	void markAllNotificationsReadForUser(@Param("userId") Integer userId);

}
