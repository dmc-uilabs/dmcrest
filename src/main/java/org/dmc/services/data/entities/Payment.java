package org.dmc.services.data.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
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
	private Date date;
	
	@Column(name = "description")
	private String description;
	
	public Payment(User user, Integer parentId, PaymentParentType type, String status, BigDecimal amount, String chargeId, Date date) {
		this.user = user;
		this.parentId = parentId;
		this.type = type;
		this.status = status;
		this.amount = amount;
		this.chargeId = chargeId;
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

}
