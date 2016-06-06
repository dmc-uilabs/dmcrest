package org.dmc.services.mappers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.entities.BaseEntity;
import org.dmc.services.models.BaseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractMapper<T extends BaseEntity, S extends BaseModel> implements Mapper<T, S> {

	@Inject
	protected MapperFactory mapperFactory;
	
	@PostConstruct
	private void init() {
		mapperFactory.registerMapper(this);
	}
	
	protected List<T> reattachEntities(List<T> entities, JpaRepository<T, Integer> dao) {
		List<Integer> ids = entities.stream()
			.filter((n) -> n.getId() != null)
			.map((n) -> n.getId())
			.collect(Collectors.toList());
		List<T> reattached = dao.findAll(ids);
		return entities.stream()
			.map((n) -> n.getId() == null ? n : reattached.remove(0))
			.collect(Collectors.toList());
	}
	
	protected S copyProperties(T source, S destination) {
		if (source == null) {
			return null;
		} else {
			BeanUtils.copyProperties(source, destination);
			return destination;
		}
	}
}
