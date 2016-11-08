package org.dmc.services.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.DirectoryService;
import org.dmc.services.data.models.DirectoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryController {

	@Inject
	private DirectoryService directoryService;

	@RequestMapping(value = "/directories", method = RequestMethod.GET)
	public List<DirectoryModel> getAllDirectories() {
		return directoryService.findAllDirectories();
	}

	@RequestMapping(value = "/directories/{directoryId}", method = RequestMethod.GET)
	public DirectoryModel getDirectoryStructure(@PathVariable("directoryId") Integer directoryId) {
		return directoryService.findDirectoryById(directoryId);
	}

	@RequestMapping(value = "/directories", method = RequestMethod.POST)
	public DirectoryModel saveDirectoryStructure(@RequestBody DirectoryModel dir) {
		return directoryService.saveDirectory(dir);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/directories/{directoryId}", method = RequestMethod.DELETE)
	public void deleteDirectory(@PathVariable Integer directoryId) {
		directoryService.deleteDirectory(directoryId);
	}

}
