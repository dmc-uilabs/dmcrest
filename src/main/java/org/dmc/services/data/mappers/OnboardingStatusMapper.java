package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.OnboardingStatus;
import org.dmc.services.data.models.OnboardingStatusModel;

/**
 * Created by kskronek on 9/1/2016.
 */
public class OnboardingStatusMapper extends AbstractMapper<OnboardingStatus, OnboardingStatusModel> {
	@Override
	public OnboardingStatus mapToEntity(OnboardingStatusModel model) {
		OnboardingStatus entity = null;

		if (model != null) {
			entity = copyProperties(model, new OnboardingStatus());
		}
		return entity;
	}

	@Override
	public OnboardingStatusModel mapToModel(OnboardingStatus entity) {
		OnboardingStatusModel model = null;

		if (entity != null) {
			model = copyProperties(entity, new OnboardingStatusModel());
		}
		return model;
	}

	@Override
	public Class<OnboardingStatus> supportsEntity() {
		return OnboardingStatus.class;
	}

	@Override
	public Class<OnboardingStatusModel> supportsModel() {
		return OnboardingStatusModel.class;
	}
}
