package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.models.DirectoryModel;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.springframework.stereotype.Component;

@Component
public class DirectoryMapper extends AbstractMapper<Directory, DirectoryModel> {

	@Inject
	DirectoryRepository directoryRepository;

	@Override
	public Directory mapToEntity(DirectoryModel model) {
		if (model == null) return null;

		Directory entity = copyProperties(model, new Directory());

		if(model.getParent() != null) {
			entity.setParent( mapToEntity(model.getParent()) );
		}

		return entity;
	}

	@Override
	public DirectoryModel mapToModel(Directory entity) {
		if (entity == null) return null;

		DirectoryModel model = copyProperties(entity, new DirectoryModel());

		if(entity.getParent() != null) {
			model.setParent( mapToModel(entity.getParent()) );
		}

		setFullDirectoryPaths(model);

		return model;
	}

	@Override
	public Class<Directory> supportsEntity() {
		return Directory.class;
	}

	@Override
	public Class<DirectoryModel> supportsModel() {
		return DirectoryModel.class;
	}

	private void setFullDirectoryPaths(DirectoryModel dir) {
		String fullPath = "/" + dir.getName();

		DirectoryModel parent = dir.getParent();
		while(parent != null) {
			fullPath = "/" + parent.getName() + fullPath;
			parent = parent.getParent();
		}

		dir.setFullPath(fullPath);
	}

}
