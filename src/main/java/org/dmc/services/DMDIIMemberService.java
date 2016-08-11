package org.dmc.services;

import org.dmc.services.data.repositories.DMDIIMemberRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by kskronek on 8/10/2016.
 */
@Service
public class DMDIIMemberService {

	@Inject
	private DMDIIMemberRepository dmdiiMemberRepository;


}
