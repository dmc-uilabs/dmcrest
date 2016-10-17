package org.dmc.services;

import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ParentDocumentService {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private UserRepository userRepository;

	public void updateParents(Document document) {
		if (document.getParentType() != null && document.getParentId() != null) {
			this.delegateToParent(document);
		}
	}

	protected void delegateToParent(Document document) {
		switch (document.getParentType()) {
			case ORGANIZATION:
				if (DocumentClass.LOGO.equals(document.getDocClass())) {
					updateOrganizationLogo(document);
				}
				break;
			case USER:
				if (DocumentClass.IMAGE.equals(document.getDocClass())) {
					updateUserImage(document);
				}

		}
	}

	@Transactional
	protected void updateOrganizationLogo(Document document) {
		Organization organization = this.organizationRepository.getOne(document.getParentId());

		if (!document.getIsDeleted()) {
			organization.setLogoImage(document.getDocumentUrl());
		} else {
			String logoUrl = organization.getLogoImage();
			if (logoUrl != null && logoUrl.equals(document.getDocumentUrl())) {
				organization.setLogoImage(null);
			}
		}

		this.organizationRepository.save(organization);
	}

	@Transactional
	protected void updateUserImage(Document document) {
		User user = this.userRepository.findOne(document.getParentId());
		if (!document.getIsDeleted()) {
			user.setImage(document.getDocumentUrl());
		} else {
			String image = user.getImage();
			if (image != null && image.equals(document.getDocumentUrl())) {
				user.setImage(null);
			}
		}
		this.userRepository.save(user);
	}
}
