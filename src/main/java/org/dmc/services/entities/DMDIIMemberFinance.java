package org.dmc.services.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "dmdii_member_finance")
public class DMDIIMemberFinance extends BaseEntity {
	
	@Id
	@SequenceGenerator(name = "dmdiiMemberFinanceSeqGen", sequenceName = "dmdii_member_finance_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dmdiiMemberFinanceSeqGen")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User dmdiiOwner;

	@Column(name = "name")
	private String name;

	@Column(name = "asset_url")
	private String assetUrl;

	public User getDmdiiOwner() {
		return dmdiiOwner;
	}

	public void setDmdiiOwner(User dmdiiOwner) {
		this.dmdiiOwner = dmdiiOwner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetUrl == null) ? 0 : assetUrl.hashCode());
		result = prime * result + ((dmdiiOwner == null) ? 0 : dmdiiOwner.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DMDIIMemberFinance other = (DMDIIMemberFinance) obj;
		if (assetUrl == null) {
			if (other.assetUrl != null)
				return false;
		} else if (!assetUrl.equals(other.assetUrl))
			return false;
		if (dmdiiOwner == null) {
			if (other.dmdiiOwner != null)
				return false;
		} else if (!dmdiiOwner.equals(other.dmdiiOwner))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


}
