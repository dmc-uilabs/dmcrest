package org.dmc.services;


import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentPlanType;
import org.dmc.services.data.entities.ServiceEntity;
import org.dmc.services.data.entities.ServiceUsePermit;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ServiceUsePermitModel;
import org.dmc.services.data.repositories.ServiceUsePermitRepository;
import org.dmc.services.payments.PaymentPlanService;
import org.dmc.services.services.ServiceEntityService;
import org.springframework.stereotype.Service;

@Service
public class ServiceUsePermitService {
	
	private final int UNLIMITED =  -1;
	private final int EMPTY = 0;
	
	@Inject
	private PaymentPlanService payPlanService;
	
	@Inject
	private ServiceUsePermitRepository supRepo;
	
	@Inject
	private MapperFactory mapperFactory;
	
	@Inject
	private ServiceEntityService servService;
	
	@Inject
	private UserService	userService;
	
	public ServiceUsePermitModel getServiceUsePermit(Integer id) {
		return mapToModel((supRepo.findOne(id)));
	}
	
	public List<ServiceUsePermitModel> getServiceUsePermitByServiceId(Integer id) {
		return mapToModel(supRepo.findByServiceId(id));
	}
	
	public List<ServiceUsePermitModel> getServiceUsePermitByOrgId(Integer id) {
		return mapToModel(supRepo.findByOrganizationId(id));
	}
	
	public ServiceUsePermitModel getServiceUsePermitByOrganizationIdAndServiceId(Integer serviceId) {
		return mapToModel(supRepo.findByOrganizationIdAndServiceId(userService.getCurrentUser().getOrganization().getId(), serviceId));
	}
	
	public ServiceUsePermit save(ServiceUsePermit sup) {
		return supRepo.save(sup);
	}
	
	public ServiceUsePermitModel processServiceUse(Integer serviceId) {
		if(payPlanService.hasPaymentPlan(serviceId)) {
			return mapToModel(supRepo.save(processUsage(findServiceUsePermit(serviceId, userService.getCurrentUser().getOrganization().getId()))));
		} else {
			return null;
		}
	}
	
	public ServiceUsePermitModel refundUse(Integer serviceId) {
		if(payPlanService.hasPaymentPlan(serviceId)) {
			return mapToModel(supRepo.save(refundUse(findServiceUsePermit(serviceId, userService.getCurrentUser().getOrganization().getId()))));
		} else {
			return null;
		}
	}
	
	public ServiceUsePermit findServiceUsePermit(Integer serviceId, Integer orgId) {
		ServiceUsePermit sup = supRepo.findByOrganizationIdAndServiceId(orgId, serviceId);
		if(sup == null) {
			ServiceEntity service = servService.getService(serviceId);
			sup = supRepo.findByOrganizationIdAndServiceId(orgId, Integer.valueOf(service.getParent()));
		}
		return sup;
	}
	
	public Boolean checkOrgServicePermit(Integer serviceId) {
		
		Boolean canRun = false;
		
		//If no payment plans, assume free usage
		if(!payPlanService.hasPaymentPlan(serviceId)) {
			return true;
		}
		
		Integer orgId = userService.getCurrentUser().getOrganization().getId();
		ServiceUsePermit sup = findServiceUsePermit(serviceId, orgId);
		
		//If a permit was found, determine uses left
		if(sup != null) {
			canRun = determineUsage(sup);
		}
		
		return canRun;
	}
	
	public ServiceUsePermit processPermitFromPlan(PaymentPlan plan, Integer quantity, User user) {
		
		Organization org = user.getOrganization();

		//Get existing permit if available
		ServiceUsePermit sup = supRepo.findByOrganizationIdAndServiceId(org.getId(), plan.getServiceId());
		
		//Create new permit
		if (sup == null) {
			sup = new ServiceUsePermit();
			sup.setOrganizationId(org.getId());
			sup.setUserId(user.getId());
			sup.setServiceId(plan.getServiceId());
		} else if(sup.getUses() == -1) {
			throw new DMCServiceException(DMCError.PaymentError, "Organization already possesses unlimited uses of service!");
		}

		if (plan.getPlanType() == PaymentPlanType.PAY_FOR_TIME) {
			sup.setExpirationDate(addDaysToDate((plan.getUses() * quantity), sup.getExpirationDate()));
		}
		//Unlimited uses
		else if (plan.getPlanType() == PaymentPlanType.PAY_ONCE) {
			sup.setUses(plan.getUses());
		} else {
			Integer uses = sup.getUses() == null ? 0 : sup.getUses();
			uses += (plan.getUses() * quantity);
			sup.setUses(uses);
		}
		
		supRepo.save(sup);

		return sup;
	}
	
	public ServiceUsePermitModel mapToModel(ServiceUsePermit sup) {
		Mapper<ServiceUsePermit, ServiceUsePermitModel> mapper = mapperFactory.mapperFor(ServiceUsePermit.class, ServiceUsePermitModel.class);
		return mapper.mapToModel(sup);
	}
	
	public List<ServiceUsePermitModel> mapToModel(Collection<ServiceUsePermit> collection) {
		Mapper<ServiceUsePermit, ServiceUsePermitModel> mapper = mapperFactory.mapperFor(ServiceUsePermit.class, ServiceUsePermitModel.class);
		return mapper.mapToModel(collection);
	}
	
	private Date addDaysToDate(Integer days, Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	private Boolean determineUsage(ServiceUsePermit sup) {
		
		Date expirDt = sup.getExpirationDate();
		Boolean canUse = expirDt != null ? expirDt.before(new Date()) : false;
		
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
	
	private ServiceUsePermit refundUse(ServiceUsePermit sup) {
		int uses = sup.getUses();
		Date expirDt = sup.getExpirationDate();
		Boolean hasExpirDt = expirDt == null ? false : true;
		
		if(uses != UNLIMITED && !hasExpirDt) {
			uses += 1;
			sup.setUses(uses);
		}
		return sup;
	}
	
	private ServiceUsePermit processUsage(ServiceUsePermit sup) {
		int uses = sup.getUses();
		Date expirDt = sup.getExpirationDate();
		Boolean beforeExpiration = expirDt != null ? expirDt.before(new Date()) : true;
		
		if(uses != UNLIMITED && beforeExpiration) {
			uses -= 1;
			sup.setUses(uses);
		}
		return sup;
	}
	
}
