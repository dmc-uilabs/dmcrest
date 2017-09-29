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
	
	public PaymentPlan getPlan(Integer id) {
		return payPlanRepo.findOne(id);
	}
	
	public List<PaymentPlan> getPlansByServiceId(Integer id) {
		return payPlanRepo.findByServiceId(id);
	}
	
	public List<PaymentPlan> getPlansByServiceIds(List<Integer> serviceIds) {
		return payPlanRepo.findByServiceIdIn(serviceIds);
	}
	
	public boolean hasPaymentPlan(Integer serviceId) {
		return !payPlanRepo.findByServiceId(serviceId).isEmpty();
	}

}
