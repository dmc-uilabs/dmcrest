package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.ResourceGroup;

public interface ResourceGroupRepository extends BaseRepository<ResourceGroup, Integer>{

	ResourceGroup findByParentTypeAndParentIdAndRoleId(String parentType, Integer parentId, Integer roleId);
}
