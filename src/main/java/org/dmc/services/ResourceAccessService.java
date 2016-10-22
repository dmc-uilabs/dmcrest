package org.dmc.services;

import java.util.List;

import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.ResourceType;
import org.dmc.services.data.entities.User;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Service;

@Service
public class ResourceAccessService {

	public Boolean hasAccess (ResourceType resourceType, Object argEntity, User owner) {
		
		//superadmins see all
		if(owner.getRoles().stream()
				.anyMatch(r->r.getRole().getRole().equals(SecurityRoles.SUPERADMIN))) {
			return true;
		}
		
		List<ResourceGroup> resGroups;
		List<ResourceGroup> userGroups = owner.getResourceGroups();
		
		switch(resourceType) {
		case DOCUMENT:
			Document doc = (Document) argEntity;
			//public documents are seen by all, VIPs have access, so do owners
			if(doc.getIsPublic() || doc.getVips().contains(owner) || doc.getOwner().equals(owner)) {
				return true;
			}
			
			resGroups = doc.getResourceGroups();
			
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
