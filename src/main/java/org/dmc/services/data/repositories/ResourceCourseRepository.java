package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.ResourceCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ResourceCourseRepository extends BaseRepository<ResourceCourse, Integer> {
	
}
