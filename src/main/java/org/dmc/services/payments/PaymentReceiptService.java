package org.dmc.services.payments;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.PaymentParentType;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.data.entities.PaymentReceipt;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.PaymentReceiptModel;
import org.dmc.services.data.repositories.PaymentReceiptRepository;
import org.springframework.stereotype.Service;

import com.stripe.model.Charge;

@Service
public class PaymentReceiptService {
	
	private final String FAILED = "failed";
	private final String SUCCESS = "succeeded";
	
	@Inject
	private PaymentReceiptRepository receiptRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public PaymentReceipt savePaymentReceipt(Integer userId, Organization org, Charge charge, PaymentPlan plan, PaymentParentType parentType) {
		return savePaymentReceipt(userId, org, plan.getServiceId(), parentType, charge.getStatus(), Math.toIntExact(charge.getAmount()),
				charge.getId(), charge.getDescription(), plan);
	}

	public PaymentReceipt savePaymentReceipt(Integer userId, Organization org, Integer parentId, PaymentParentType parentType, String status, Integer amount,
			String chargeId, String desc, PaymentPlan plan) {
		Integer orgId = null;
		Integer orgBalance = 0;
		if (org == null && parentType == PaymentParentType.ORGANIZATION) {
			orgId = parentId;
		} else {
			orgId = org.getId();
			orgBalance = org.getAccountBalance();
		}
		PaymentReceipt receipt = new PaymentReceipt(userId, orgId, parentId, parentType, status, amount, chargeId, desc,
				plan, orgBalance);
		return receiptRepository.save(receipt);
	}
	
	public PaymentReceipt savePaymentReceipt(PaymentReceipt receipt) {
		if (receipt.getOrgId() == null && receipt.getType() == PaymentParentType.ORGANIZATION) {
			receipt.setOrgId(receipt.getParentId());
		} 
		return receiptRepository.save(receipt);
	}
	
	public List<PaymentReceiptModel> getPaymentReceiptsByOrgId(Integer orgId) {
		return getReceiptMapper().mapToModel(receiptRepository.findByOrganizationIdAndStatus(orgId, SUCCESS));
	}
	
	public Integer getPaymentFailureCount(User user) {
		Date startDate = DateUtils.addDays(new Date(), -1);
		Date endDate = DateUtils.addDays(new Date(), 1);
		
		return receiptRepository.countByUserIdAndStatusAndDateBetween(user.getId(), FAILED, startDate, endDate);
	}
	
	private Mapper<PaymentReceipt, PaymentReceiptModel> getReceiptMapper() {
		return mapperFactory.mapperFor(PaymentReceipt.class, PaymentReceiptModel.class);
	}

}