package org.dmc.services;

import java.util.List;

import org.dmc.services.data.entities.DomeServer;
import org.dmc.services.data.repositories.DomeServerRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomeServerService {
	@Autowired
	private DomeServerRepository serverRepo;

	@Autowired
	private UserRepository userRepo;
	
	static final Logger LOG = LoggerFactory.getLogger(ServerAccessService.class);
	
	//returns all public servers and user's own servers
	public List<DomeServer> findAllServers(Integer userId){
		LOG.info("Get all accessible servers: (user: {})", userId);
		return serverRepo.findAllServers(userId);
	}
	
}
