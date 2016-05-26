package org.dmc.services.dmdiimember;

import java.sql.Date;

import javax.persistence.*;

import org.dmc.services.dmdiitype.DMDIIType;
import org.dmc.services.organization.Organization;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "organization_dmdii_member")
public class DMDIIMember {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "dmdii_type_id")
	private DMDIIType dmdiiType;
	@ManyToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "expire_date")
	private Date expireDate;


	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public DMDIIType getDmdiiType() {
		return this.dmdiiType;
	}
	
	public void setDmdiiType(DMDIIType dmdiiType) {
		this.dmdiiType = dmdiiType;
	}

	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
