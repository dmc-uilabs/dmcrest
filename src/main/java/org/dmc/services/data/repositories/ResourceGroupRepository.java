package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.Role;

public interface ResourceGroupRepository extends BaseRepository<ResourceGroup, Integer>{

	ResourceGroup findByParentTypeAndParentIdAndRole(DocumentParentType parentType, Integer parentId, Role role);
}
