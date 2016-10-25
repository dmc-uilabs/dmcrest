package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ResourceGroup;

public interface ResourceGroupRepository extends BaseRepository<ResourceGroup, Integer>{

	ResourceGroup findByParentTypeAndParentIdAndRoleId(DocumentParentType parentType, Integer parentId, Integer roleId);
}
