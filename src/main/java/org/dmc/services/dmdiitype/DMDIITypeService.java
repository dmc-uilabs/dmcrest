package org.dmc.services.dmdiitype;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class DMDIITypeService {

	@Inject
	private DMDIITypeDao dmdiiTypeDao;
	
	public List<DMDIIType> findAll() {
		return dmdiiTypeDao.findAll();
	}
	
}
