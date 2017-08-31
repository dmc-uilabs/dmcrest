package org.dmc.services.data.models;

import java.math.BigDecimal;
import java.util.Date;

import org.dmc.services.data.entities.PaymentParentType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PaymentReceiptModel extends BaseModel {

	private Integer parentId;

	private PaymentParentType type;

	private String status;

	private BigDecimal amount;
	
	private BigDecimal balance;
	
	private String description;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
	private Date date;

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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
