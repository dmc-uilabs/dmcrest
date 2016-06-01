package org.dmc.services.dmdiimember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "dmdii_member_finance")
public class DMDIIMemberFinance {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User dmdiiOwner;

	@Column(name = "name")
	private String name;

	@Column(name = "asset_url")
	private String assetUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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


}
