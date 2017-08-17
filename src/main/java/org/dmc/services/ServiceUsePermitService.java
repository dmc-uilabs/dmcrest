package org.dmc.services;


import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUsePermitService {
	
	private final int UNLIMITED =  -1;
	private final int EMPTY = 0;
	
	@Inject
	private PaymentPlanRepository payPlanRepo;
	
	@Inject
	private ServiceUsePermitRepository supRepo;
	
	@Inject
	private OrganizationUserRepository orgUserRepo;
	
	public Boolean checkUserServicePermit(Integer serviceId) {
		return checkUserServicePermit(serviceId, getCurrentUserId());
	}
	
	public Boolean checkUserServicePermit(Integer serviceId, Integer userId) {
		Boolean canRun = false;
		ServiceUsePermit sup = supRepo.findByUserIdAndServiceId(userId, serviceId);
		if(sup == null) {
			sup = supRepo.findByOrganizationIdAndServiceId(orgUserRepo.findByUserId(userId).getOrganization().getId(), serviceId);
		}
		if(sup != null) {
			determineUsage(sup);
		}
		List<PaymentPlan> plans = payPlanRepo.findByServiceId(serviceId);
		//If no payment plans, assume free usage
		if(plans.isEmpty()) {
			canRun = true;
		}
		
		return canRun;
	}
	
	private Boolean determineUsage(ServiceUsePermit sup) {
		Boolean canUse = false;
		
		if(sup.getExpirationDate() != null) {
			Date now = new Date();
			canUse = sup.getExpirationDate().before(now);
		} else {
			Integer uses = sup.getUses();
			if(uses == UNLIMITED) {
				canUse = true;
			} else if(uses > EMPTY) {
				sup.setUses(uses -= 1);
				supRepo.save(sup);
				canUse = true;
			}
		}
			
		return canUse;
	}
	
	public ServiceUsePermit getServiceUsePermit(Integer id) {
		return supRepo.findOne(id);
	}
	
	public List<ServiceUsePermit> getServiceUsePermitByServiceId(Integer id) {
		return supRepo.findByServiceId(id);
	}
	
	public List<ServiceUsePermit> getServiceUsePermitByOrgId(Integer id) {
		return supRepo.findByOrganizationId(id);
	}
	
	private Integer getCurrentUserId() {
		return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}

}
