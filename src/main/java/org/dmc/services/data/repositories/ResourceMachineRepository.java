package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.ResourceMachine;

import java.util.List;

//Repositories are essentially DAOs. They are the class that queries that DB. Simple queries extend from
//the base repo. You can find there documentation online here http://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
public interface ResourceMachineRepository extends BaseRepository<ResourceMachine, Integer> {



	//to make a custom queries, specify the type of the return object and the input needed.
	//then the methods described here under section 2.2 http://docs.spring.io/spring-data/jpa/docs/1.3.0.RELEASE/reference/html/jpa.repositories.html
	List<ResourceMachine> findBybay_id(Integer bayId);


}
