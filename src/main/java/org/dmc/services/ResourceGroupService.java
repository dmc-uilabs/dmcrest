package org.dmc.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.SecurityRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ResourceGroupService {
	
	@Inject
	private ResourceGroupRepository resourceGroupRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private DocumentRepository documentRepository;
	
	static final Logger LOG = LoggerFactory.getLogger(ResourceGroupService.class);
	
	@Transactional
	public void newCreate (DocumentParentType parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);
    	List<String> roles = Arrays.asList(SecurityRoles.ADMIN, SecurityRoles.MEMBER);

		for(String role: roles) {
			ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
			if(group == null) {
				group = new ResourceGroup(parentType, parentId, role);
			}
			resourceGroupRepository.save(group);
		}
	}
	
	@Transactional
	public void removeAll(DocumentParentType parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);
    	List<String> roles = Arrays.asList(SecurityRoles.ADMIN, SecurityRoles.MEMBER);

		for(String role: roles) {
			ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
			if(group != null) {
				resourceGroupRepository.delete(group);
			}
		}
	}
	
	@Transactional
	public User removeUserResourceGroup(User user, DocumentParentType parentType, Integer parentId, String role) {
		Assert.notNull(user);
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		Assert.notNull(role);
		
		ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
		List<ResourceGroup> groups = user.getResourceGroups();
		if (group != null) {
			groups.remove(group);
			user.setResourceGroups(groups);
			return userRepository.save(user);
		}
		
		return user;
	}
	
	@Transactional
	public User addUserResourceGroup(User user, DocumentParentType parentType, Integer parentId, String role) {
		Assert.notNull(user);
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		Assert.notNull(role);
		
		ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
		List<ResourceGroup> userGroups = user.getResourceGroups();
		if(userGroups == null){
			userGroups = new ArrayList<>();
		}
		if (!userGroups.contains(group)) {
			userGroups.add(group);
			user.setResourceGroups(userGroups);
			return userRepository.save(user);
		}
		
		return user;
	}
	
	public Document updateDocumentResourceGroups(Document doc, String role) {
		Assert.notNull(doc);
		Assert.notNull(role);
		
		List<ResourceGroup> groups = doc.getResourceGroups();

		ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(doc.getParentType(), doc.getParentId(), role);
		
		if(CollectionUtils.isNotEmpty(groups)) {
			if(role.equals(SecurityRoles.ADMIN)) {
				if(!groups.contains(group)) {
					//if ADMIN group isn't already here, add it
					groups.add(group);
				}
				
				//if access was MEMBER before, remove it now
				ResourceGroup memGroup = resourceGroupRepository.findByParentTypeAndParentIdAndRole(doc.getParentType(), doc.getParentId(), SecurityRoles.MEMBER);
				
				if(groups.contains(memGroup)) {
					groups.remove(memGroup);
				}
				
			} else if (role.equals(SecurityRoles.MEMBER)) {
				if(!groups.contains(group)) {
					//if MEMBER group isn't already here, add it
					groups.add(group);
					
					//if ADMIN group isn't already here, add it
					ResourceGroup adminGroup = resourceGroupRepository.findByParentTypeAndParentIdAndRole(doc.getParentType(), doc.getParentId(), SecurityRoles.ADMIN);
					
					if(!groups.contains(adminGroup)) {
						groups.add(adminGroup);
					}
				}						
			} else if (role.equals(SecurityRoles.PUBLIC)) {
				//we don't need resource group for this parentType and parentId, remove them if they exist
				ResourceGroup adminGroup = resourceGroupRepository.findByParentTypeAndParentIdAndRole(doc.getParentType(), doc.getParentId(), SecurityRoles.ADMIN);
				ResourceGroup memGroup = resourceGroupRepository.findByParentTypeAndParentIdAndRole(doc.getParentType(), doc.getParentId(), SecurityRoles.MEMBER);
				if (groups.contains(adminGroup)) {
					groups.remove(adminGroup);
				}
				
				if (groups.contains(memGroup)) {
					groups.remove(memGroup);
				}
			}
		}
		
		doc.setResourceGroups(groups);
		
		return doc;
	}
}
