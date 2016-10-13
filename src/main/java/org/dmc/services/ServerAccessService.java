package org.dmc.services;

import org.dmc.services.data.entities.DomeServer;
import org.dmc.services.data.entities.ServerAccess;
import org.dmc.services.data.repositories.DomeServerRepository;
import org.dmc.services.data.repositories.ServerAccessRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.SecurityRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerAccessService {
	@Autowired
	private ServerAccessRepository accessRepo;
	
	@Autowired
	private DomeServerRepository serverRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	static final Logger LOG = LoggerFactory.getLogger(ServerAccessService.class);
	
	public ServerAccess findGroupByName(String name){
		return accessRepo.findOneByName(name);
	}
	
	private Boolean isAuth(String eppn){
		return userRepo.findByUsername(eppn).getRoles().stream()
				.anyMatch(r->r.getRole().getRole().equals(SecurityRoles.SUPERADMIN));
	}
	
	@Transactional
	public void addServerToGlobalById(Integer serverId, String eppn) throws Exception{
		LOG.info("Add server to global group: (server id: {}) (user: {})", serverId, eppn);
		
		//if user is a superadmin, add the server, otherwise error
		if(isAuth(eppn)){
			ServerAccess global = accessRepo.findOneByName("global");
			DomeServer server = serverRepo.findById(serverId);
			global.getServers().add(server);
			accessRepo.save(global);	
		}
		else {
			LOG.error("Make server public failed, not authorized: (user: {})", eppn);
			throw new DMCServiceException(DMCError.CannotChangeServerAccess,
					"Do not have permission.");
		}
	}
	
	@Transactional
	public void removeServerFromGlobalById(Integer serverId){
		LOG.info("Remove server from global group: (server id: {})", serverId);
		
		ServerAccess global = accessRepo.findOneByName("global");
		global.getServers().removeIf(s->s.getId()==serverId);
		accessRepo.save(global);
	}
	
	@Transactional
	public void changeServerPublicAccess(Integer serverId, String eppn) throws Exception{
		LOG.info("Change server from global group: (server id: {}) (user: {})", serverId, eppn);
		
		//if user is a superadmin, update the server, otherwise error
		if(isAuth(eppn)){
			ServerAccess global = accessRepo.findOneByName("global");
			if(!global.getServers().removeIf(s->s.getId()==serverId)){
				global.getServers().add(serverRepo.findById(serverId));
			}	
			accessRepo.save(global);
		}
		else {
			LOG.error("Change server access failed, not authorized: (user: {})", eppn);
			throw new DMCServiceException(DMCError.CannotChangeServerAccess,
					"Do not have permission.");
		}
	}
		
}
