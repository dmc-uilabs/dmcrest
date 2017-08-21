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
		return checkOrgServicePermit(serviceId, orgUserRepo.findByUserId(userId).getOrganization().getId());
	}
	
	public Boolean checkOrgServicePermit(Integer serviceId, Integer orgId) {
		Boolean canRun = false;
		List<PaymentPlan> plans = payPlanRepo.findByServiceId(serviceId);
		
		//If no payment plans, assume free usage
		if(plans.isEmpty()) {
			return true;
		}
		
		ServiceUsePermit sup = supRepo.findByOrganizationIdAndServiceId(orgId, serviceId);
		
		//If a permit was found, determine uses left
		if(sup != null) {
			canRun = determineUsage(sup);
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
				canUse = true;
			}
		}
			
		return canUse;
	}
	
	private void processUsage(ServiceUsePermit sup) {
		//Only deduct from uses if there's no expiration date
		if(sup.getExpirationDate() == null) {
			int uses = sup.getUses();
			uses -= 1;
			sup.setUses(uses);
			supRepo.save(sup);
		}
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
	
	public ServiceUsePermit getServiceUsePermitByOrganizationIdAndServiceId(Integer orgId, Integer serviceId) {
		return supRepo.findByOrganizationIdAndServiceId(orgId, serviceId);
	}
	
	private Integer getCurrentUserId() {
		return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}

}
