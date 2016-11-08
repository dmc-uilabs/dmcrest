package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.ResourceType;
import org.dmc.services.data.entities.User;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Service;

@Service
public class ResourceAccessService {
	
	@Inject
	private DocumentService documentService;

	public Boolean hasAccess (ResourceType resourceType, Object argEntity, User requester) {
		
		//superadmins see all
		if(requester.getRoles().stream()
				.anyMatch(r->r.getRole().getRole().equals(SecurityRoles.SUPERADMIN))) {
			return true;
		}
		
		List<ResourceGroup> resGroups;
		List<ResourceGroup> userGroups = requester.getResourceGroups();
		
		switch(resourceType) {
		case DOCUMENT:
			Document doc = (Document) argEntity;
			//public documents are seen by all, VIPs have access, so do owners
			if(doc.getIsPublic() || doc.getOwner().equals(requester)) {
				return true;
			} else if (CollectionUtils.isNotEmpty(doc.getVips())) {
				return doc.getVips().contains(requester);
			}
			
			resGroups = doc.getResourceGroups();
			
			if(userGroups != null) {
				for(ResourceGroup group : userGroups) {
					if(group.getParentType() == DocumentParentType.PROJECT) {
						if(documentService.findServiceDocumentsByProjectId(group.getParentId())
						.stream()
						.anyMatch(d -> d.getId().equals(doc.getId()))) {
							return true;
						}
					}
				}
			}
			
			break;
		default:
			resGroups = null;
			break;
		}
		
		if (resGroups != null && userGroups != null) {
			for (ResourceGroup resGroup : resGroups) {
				for (ResourceGroup userGroup : userGroups) {
					if(resGroup.equals(userGroup)) {
						return true;
					}
				}
			} 
		}
		return false;
	}
}
