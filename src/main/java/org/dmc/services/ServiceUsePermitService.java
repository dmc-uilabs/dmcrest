package org.dmc.services;


import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.ServiceEntity;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ServiceUsePermitModel;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.data.repositories.PaymentPlanRepository;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.services.ServiceEntityService;
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
	
	@Inject
	private ServiceEntityService servService;
	
	public void processServiceUse(Integer serviceId) {
		if(hasPaymentPlan(serviceId)) {
			processUsage(findServiceUsePermit(serviceId, getCurrentOrgId()));
		}
	}
	
	public void refundUse(Integer serviceId) {
		if(hasPaymentPlan(serviceId)) {
			refundUse(findServiceUsePermit(serviceId, getCurrentOrgId()));
		}
	}
	
	public void processServiceUse(Integer serviceId, Integer orgId) {
		if(hasPaymentPlan(serviceId)) {
			ServiceUsePermit sup = findServiceUsePermit(serviceId, orgId);
			processUsage(sup);
		}
	}
	
	public Boolean checkServicePermit(Integer serviceId) {
		return checkOrgServicePermit(serviceId, getCurrentOrgId());
	}
	
	public Boolean checkOrgServicePermit(Integer serviceId, Integer orgId) {
		Boolean canRun = false;
		
		//If no payment plans, assume free usage
		if(!hasPaymentPlan(serviceId)) {
			return true;
		}
		
		ServiceUsePermit sup = findServiceUsePermit(serviceId, orgId);
		
		//If a permit was found, determine uses left
		if(sup != null) {
			canRun = determineUsage(sup);
		}
		
		return canRun;
	}
	
	private ServiceUsePermit findServiceUsePermit(Integer serviceId, Integer orgId) {
		ServiceUsePermit sup = supRepo.findByOrganizationIdAndServiceId(orgId, serviceId);
		if(sup == null) {
			ServiceEntity service = servService.getService(serviceId);
			sup = supRepo.findByOrganizationIdAndServiceId(orgId, Integer.valueOf(service.getParent()));
		}
		return sup;
	}
	
	private Boolean hasPaymentPlan(Integer serviceId) {
		List<PaymentPlan> plans = payPlanRepo.findByServiceId(serviceId);
		
		return !plans.isEmpty();
	}

	private Boolean determineUsage(ServiceUsePermit sup) {
		
		Date expirDt = sup.getExpirationDate();
		Boolean canUse = expirDt != null ? expirDt.before(new Date()) : true;
		
		if(!canUse) {
			Integer uses = sup.getUses();
			if(uses == UNLIMITED) {
				canUse = true;
			} else if(uses > EMPTY) {
				canUse = true;
			}
		}
			
		return canUse;
	}
	
	private void refundUse(ServiceUsePermit sup) {
		int uses = sup.getUses();
		Date expirDt = sup.getExpirationDate();
		Boolean beforeExpiration = expirDt != null ? expirDt.before(new Date()) : true;
		
		if(uses != UNLIMITED && !beforeExpiration) {
			uses += 1;
			sup.setUses(uses);
			supRepo.save(sup);
		}
	}
	
	private void processUsage(ServiceUsePermit sup) {
		int uses = sup.getUses();
		Date expirDt = sup.getExpirationDate();
		Boolean beforeExpiration = expirDt != null ? expirDt.before(new Date()) : true;
		
		if(uses != UNLIMITED && beforeExpiration) {
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
	
	private Integer getCurrentOrgId() {
		Integer userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		return orgUserRepo.findByUserId(userId).getOrganization().getId();
	}
	
	public ServiceUsePermitModel mapToModel(ServiceUsePermit sup) {
		return getSupMapper().mapToModel(sup);
	}
	
	private Mapper<ServiceUsePermit, ServiceUsePermitModel> getSupMapper() {
		return mapperFactory.mapperFor(ServiceUsePermit.class, ServiceUsePermitModel.class);
	}

}
