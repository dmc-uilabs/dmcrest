package org.dmc.services;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DirectoryModel;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.dmc.services.data.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectoryService {

	@Inject
	private DirectoryRepository directoryRepository;

	@Inject
	private DocumentRepository documentRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<DirectoryModel> findAllDirectories() {
		Mapper<Directory, DirectoryModel> directoryMapper = mapperFactory.mapperFor(Directory.class, DirectoryModel.class);
		List<Directory> directories = directoryRepository.findByParent(null);
		List<DirectoryModel> directoryModels = directoryMapper.mapToModel(directories);
		return directoryModels;
	}

	public DirectoryModel findDirectoryById(Integer id) {
		Mapper<Directory, DirectoryModel> directoryMapper = mapperFactory.mapperFor(Directory.class, DirectoryModel.class);
		Directory dir = directoryRepository.findOne(id);
		DirectoryModel directory = directoryMapper.mapToModel(dir);
		return directory;
	}

	@Transactional
	public DirectoryModel saveDirectory(DirectoryModel dir) {
		Mapper<Directory, DirectoryModel> mapper = mapperFactory.mapperFor(Directory.class, DirectoryModel.class);

		Directory parent = directoryRepository.save(mapper.mapToEntity(dir));
		DirectoryModel model = mapper.mapToModel(parent);

		for(DirectoryModel child: dir.getChildren()) {
			child.setParent(parent.getId());
			child = saveDirectory(child);
			model.getChildren().add(child);
		}

		return model;
	}

	@Transactional
	public void deleteDirectory(Integer directoryId) {
		Directory dir = directoryRepository.findOne(directoryId);

		recursiveDelete(dir);
	}

	private void recursiveDelete(Directory dir) {
		for(Iterator<Directory> iterator = dir.getChildren().iterator(); iterator.hasNext(); ) {
			Directory child = iterator.next();
			iterator.remove();
			recursiveDelete(child);
		}

		// soft delete current directory's documents
		List<Document> docs = documentRepository.findByDirectory(dir);
		docs.stream().forEach((a) -> a.setIsDeleted(true));
		documentRepository.save(docs);

		// soft delete current directory
		dir.setIsDeleted(true);
		directoryRepository.save(dir);
	}
}
