package org.dmc.services;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.UserRepository;
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
	
	private Boolean hasAccess(Integer userId, String parentType, Integer parentId, String resourceType, Integer resourceId) {
		List<ResourceGroup> resGroups;
		List<ResourceGroup> userGroups = userRepository.findOne(userId).getResourceGroups();
		
		switch(resourceType.toUpperCase()) {
		case "DOCUMENT":
			resGroups = documentRepository.findOne(resourceId).getResourceGroups();
			break;
		default:
			resGroups = null;
			break;
		}
		
		List<ResourceGroup> matchList = userGroups.stream().
				filter(resGroups::contains).
				collect(toList());
		
		if(matchList.isEmpty())
			return false;
		else
			return true;
	}

	@Transactional
	public void newCreate (String parentType, Integer parentId) {
		Assert.notNull(parentType);
		Assert.notNull(parentId);

		for(int i = 2; i < 5; i++) {
			ResourceGroup group = new ResourceGroup();
			group.setParentType(parentType);
			group.setParentId(parentId);
			group.setRoleId(i);
			resourceGroupRepository.save(group);
		}
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
