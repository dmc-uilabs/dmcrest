package org.dmc.services.search;


import org.dmc.services.company.Company;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.services.Service;
import org.dmc.services.users.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 200005921 on 2/1/2016.
 */

@RestController
public class SearchController implements SearchInterface {

    private SearchImpl searchImpl;


    public SearchController () {
        searchImpl = new SearchImpl();
    }

    @Override
    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public SearchResult search (@PathVariable("query")String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.search(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchComponents/{query}", method = RequestMethod.GET)
    public List<Component> searchComponents(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchComponents(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchServices/{query}", method = RequestMethod.GET)
    public List<Service> searchServices(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchServices(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchProjects/{query}", method = RequestMethod.GET)
    public List<Project> searchProjects(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchProjects(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchUsers/{query}", method = RequestMethod.GET)
    public List<User> searchUsers(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchUsers(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchCompanies/{query}", method = RequestMethod.GET)
    public List<Company> searchCompanies(String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchCompanies(query, userEPPN);
    }
}
