package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.Notification;
import org.dmc.services.data.entities.Notification.NotificationType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.MiniUserModel;
import org.dmc.services.data.models.NotificationModel;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper extends AbstractMapper<Notification, NotificationModel> {

	@Override
	public Notification mapToEntity(NotificationModel model) {
		if (model == null) return null;
		
		Notification entity = copyProperties(model, new Notification());
		
		Mapper<User, MiniUserModel> userMapper = mapperFactory.mapperFor(User.class, MiniUserModel.class);
		
		entity.setCreatedBy(userMapper.mapToEntity(model.getCreatedBy()));
		entity.setCreatedFor(userMapper.mapToEntity(model.getCreatedFor()));
		entity.setType(NotificationType.valueOf(model.getType()));
		
		return entity;
	}

	@Override
	public NotificationModel mapToModel(Notification entity) {
		if (entity == null) return null;
		
		NotificationModel model = copyProperties(entity, new NotificationModel());
		
		Mapper<User, MiniUserModel> userMapper = mapperFactory.mapperFor(User.class, MiniUserModel.class);

		model.setCreatedBy(userMapper.mapToModel(entity.getCreatedBy()));
		model.setCreatedFor(userMapper.mapToModel(entity.getCreatedFor()));
		model.setType(entity.getType().toString());
		
		return model;
	}

	@Override
	public Class<Notification> supportsEntity() {
		return Notification.class;
	}

	@Override
	public Class<NotificationModel> supportsModel() {
		return NotificationModel.class;
	}

}
