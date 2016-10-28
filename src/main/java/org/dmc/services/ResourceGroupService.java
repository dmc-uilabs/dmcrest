package org.dmc.services;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.Role;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.RoleRepository;
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
	private RoleRepository roleRepository;
	
	static final Logger LOG = LoggerFactory.getLogger(ResourceGroupService.class);
	
	@Transactional
	public void newCreate (DocumentParentType parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);
    	List<Role> roles = Arrays.asList(roleRepository.findByRole(SecurityRoles.ADMIN), roleRepository.findByRole(SecurityRoles.MEMBER));

		for(Role role: roles) {
			ResourceGroup group = new ResourceGroup(parentType, parentId, role);
			resourceGroupRepository.save(group);
		}
	}
	
	@Transactional
	public void removeAll(DocumentParentType parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);
    	List<Role> roles = Arrays.asList(roleRepository.findByRole(SecurityRoles.ADMIN), roleRepository.findByRole(SecurityRoles.MEMBER));

		for(Role role: roles) {
			ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
			resourceGroupRepository.delete(group);
		}
	}
	
	@Transactional
	public User removeResourceGroup(User user, DocumentParentType parentType, Integer parentId, String roleName) {
		Assert.notNull(user);
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		Assert.notNull(roleName);
		
		Role role = roleRepository.findByRole(roleName);
		
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
	public User addResourceGroup(User user, DocumentParentType parentType, Integer parentId, String roleName) {
		Assert.notNull(user);
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		Assert.notNull(roleName);
		
		Role role = roleRepository.findByRole(roleName);
		
		ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(parentType, parentId, role);
		List<ResourceGroup> userGroups = user.getResourceGroups();
		if (!userGroups.contains(group)) {
			userGroups.add(group);
			user.setResourceGroups(userGroups);
			return userRepository.save(user);
		}
		
		return user;
	}
}
