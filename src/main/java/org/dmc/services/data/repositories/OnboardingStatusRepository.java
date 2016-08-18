package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kskronek on 8/16/2016.
 */
public interface OnboardingStatusRepository extends JpaRepository<OnboardingStatus, Integer> {
}
