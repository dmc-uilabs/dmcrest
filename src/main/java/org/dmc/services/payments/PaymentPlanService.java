package org.dmc.services.payments;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentPlanService {
	
	@Inject 
	private PaymentPlanRepository payPlanRepo;
	
	public List<PaymentPlan> getPlans(Integer id) {
		return payPlanRepo.findByServiceId(id);
	}
	
	public List<PaymentPlan> getPlans(List<Integer> serviceIds) {
		return payPlanRepo.findByServiceIdIn(serviceIds);
	}

}
