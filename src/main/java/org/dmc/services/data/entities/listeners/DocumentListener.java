package org.dmc.services.data.entities.listeners;


import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.PostPersist;

@Component
public class DocumentListener {

	static private OrganizationRepository organizationRepository;

	@Autowired
	public void init(OrganizationRepository organizationRepository)
	{
		DocumentListener.organizationRepository = organizationRepository;
	}

	@PostPersist
	public void handleDocumentPersist(Document document){
		Assert.notNull(document.getParentId());
		Assert.notNull(document.getParentType());
		this.delegateToParent(document);
	}

	protected void delegateToParent(Document document){
		switch (document.getParentType()){
			case ORGANIZATION:
				if(DocumentClass.LOGO.equals(document.getDocClass())){
					updateOrganizationLogo(document.getParentId(), document.getDocumentUrl());
				}
		}
	}

	@Transactional
	protected void updateOrganizationLogo(Integer id, String logoUrl){
		Organization organization = this.organizationRepository.getOne(id);
		organization.setLogoImage(logoUrl);
		this.organizationRepository.save(organization);
	}
}
