package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIMemberService {

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;
	
	public List<DMDIIMember> findPage(Integer pageNumber, Integer pageSize) {
		Page<DMDIIMember> page = dmdiiMemberDao.findAll(new PageRequest(pageNumber, pageSize));
		return page.getContent();
	}
	
}
