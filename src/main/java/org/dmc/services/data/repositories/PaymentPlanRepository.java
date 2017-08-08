package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.PaymentPlan;

public interface PaymentPlanRepository extends BaseRepository<PaymentPlan, Integer> {
	
	List<PaymentPlan> findByServiceId(Integer serviceId);

}
