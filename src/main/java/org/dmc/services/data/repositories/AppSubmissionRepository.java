package org.dmc.services.data.repositories;

import java.util.Set;

import org.dmc.services.data.entities.AppSubmission;
import org.springframework.data.jpa.repository.Query;

public interface AppSubmissionRepository extends BaseRepository<AppSubmission, Integer> {
	
	@Query("SELECT aps.appName from AppSubmission aps")
	Set<String> getAppNames();

}
