package org.dmc.services.data.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "payment_receipt")
public class PaymentReceipt extends BaseEntity {
	
	@Transient
	private final String DEFAULT_DESC = "Payment for ";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "organization_id")
	private Integer orgId;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "parent_type")
	@Enumerated(EnumType.STRING)
	private PaymentParentType type;

	@Column(name = "payment_status")
	private String status;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "charge_id")
	private String chargeId;

	@Column(name = "charge_dt")
	private Date date = new Date();

	@Column(name = "description")
	private String description;
	
	@Column(name = "balance")
	private BigDecimal balance;

	@JoinColumn(name = "payment_plan_id", referencedColumnName = "id")
	@ManyToOne
	private PaymentPlan paymentPlan;
	
	public PaymentReceipt() {}

	public PaymentReceipt(Integer userId, Integer orgId, Integer parentId, PaymentParentType type, String status, BigDecimal amount,
			String chargeId, String description, PaymentPlan plan, BigDecimal balance) {
		this.userId = userId;
		this.orgId = orgId;
		this.parentId = parentId;
		this.type = type;
		this.status = status;
		this.amount = amount;
		this.chargeId = chargeId;
		this.description = description;
		this.paymentPlan = plan;
		this.balance = balance;
	}
	
	public PaymentReceipt(Integer userId, Integer orgId, Integer parentId, PaymentParentType type, String status, Integer amount,
			String chargeId, String description, PaymentPlan plan, Integer balance) {
		this.userId = userId;
		this.orgId = orgId;
		this.parentId = parentId;
		this.type = type;
		this.status = status;
		this.amount = BigDecimal.valueOf(amount, 2);
		this.chargeId = chargeId;
		this.description = description;
		this.paymentPlan = plan;
		this.balance = BigDecimal.valueOf(balance, 2);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getOrgId() {
		return orgId;
	}
	
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public PaymentParentType getType() {
		return type;
	}

	public void setType(PaymentParentType type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public void setAmount(Integer amount) {
		this.amount = BigDecimal.valueOf(amount, 2);
	}
	
	public void setAmount(Long amount) {
		this.amount = BigDecimal.valueOf(amount, 2);
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public void setBalance(Integer balance) {
		this.balance = BigDecimal.valueOf(balance, 2);
	}

	public PaymentPlan getPaymentPlan() {
		return paymentPlan;
	}

	public void setPaymentPlan(PaymentPlan paymentPlan) {
		this.paymentPlan = paymentPlan;
	}
	
}
