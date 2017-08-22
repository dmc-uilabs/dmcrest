package org.dmc.services.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dmc.services.services.Service;

@Entity
@Table(name = "service_use_permit")
public class ServiceUsePermit extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "remaining_uses")
	private Integer remainingUses;
	
	@Column(name = "expire_dt")
	private Date expirationDate;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @returns -1 for unlimited uses
	 */
	public Integer getUses() {
		remainingUses = remainingUses == null ? 0 : remainingUses;
		return remainingUses;
	}

	public void setUses(Integer uses) {
		this.remainingUses = uses;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
