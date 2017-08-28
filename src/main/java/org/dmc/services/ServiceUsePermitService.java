package org.dmc.services;


import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ServiceUsePermitModel;
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
	
	@Inject
	private MapperFactory mapperFactory;
	
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
		Integer uses = sup.getUses();
		
		if(sup.getExpirationDate() != null) {
			Date now = new Date();
			canUse = sup.getExpirationDate().before(now);
		}
		
		if(!canUse) {
			if(uses == UNLIMITED) {
				canUse = true;
			} else if(uses > EMPTY) {
				canUse = true;
			}
		}
			
		return canUse;
	}
	
	private void processUsage(ServiceUsePermit sup) {
		int uses = sup.getUses();
		
		if(uses != UNLIMITED) {
			uses -= 1;
			sup.setUses(uses);
			supRepo.save(sup);
		}
		
	}
	
	public ServiceUsePermitModel getServiceUsePermit(Integer id) {
		return getSupMapper().mapToModel((supRepo.findOne(id)));
	}
	
	public List<ServiceUsePermitModel> getServiceUsePermitByServiceId(Integer id) {
		return getSupMapper().mapToModel(supRepo.findByServiceId(id));
	}
	
	public List<ServiceUsePermitModel> getServiceUsePermitByOrgId(Integer id) {
		return getSupMapper().mapToModel(supRepo.findByOrganizationId(id));
	}
	
	public ServiceUsePermit getServiceUsePermitByOrganizationIdAndServiceId(Integer orgId, Integer serviceId) {
		return supRepo.findByOrganizationIdAndServiceId(orgId, serviceId);
	}
	
	private Integer getCurrentUserId() {
		return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}
	
	public ServiceUsePermitModel mapToModel(ServiceUsePermit sup) {
		return getSupMapper().mapToModel(sup);
	}
	
	private Mapper<ServiceUsePermit, ServiceUsePermitModel> getSupMapper() {
		return mapperFactory.mapperFor(ServiceUsePermit.class, ServiceUsePermitModel.class);
	}

}
