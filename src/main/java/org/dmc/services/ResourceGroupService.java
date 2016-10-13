package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ResourceGroupService {
	
	@Inject
	private ResourceGroupRepository resourceGroupRepository;
	
	@Inject
	private UserRepository userRepository;

	public Boolean newCreate (String parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		
		try {
			for(int i = 2; i < 5; i++) {
				ResourceGroup group = new ResourceGroup();
				group.setParentType(parentType);
				group.setParentId(parentId);
				group.setRoleId(i);
				resourceGroupRepository.save(group);
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public User removeResourceGroup(User user, String parentType, Integer parentId, Integer roleId) {
		Assert.notNull(user);
		Assert.notNull(parentType);
		Assert.notNull(parentId);
		Assert.notNull(roleId);
		
		ResourceGroup group = resourceGroupRepository.findByParentTypeAndParentIdAndRoleId(parentType, parentId, roleId);
		List<ResourceGroup> groups = user.getResourceGroups();
		groups.remove(group);
		user.setResourceGroups(groups);
		return userRepository.save(user);
	}
}
