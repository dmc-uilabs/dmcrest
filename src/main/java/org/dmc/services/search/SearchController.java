package org.dmc.services.search;


import org.dmc.services.company.Company;
import org.dmc.services.components.Component;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.profile.Profile;
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
    private SearchQueueImpl searchQueueImpl;


    public SearchController () {
        searchImpl = new SearchImpl();
        searchQueueImpl = new SearchQueueImpl();

        searchQueueImpl.init();
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
    @RequestMapping(value = "/searchMarketplace/{query}", method = RequestMethod.GET)
    public List<Service> searchMarketplace(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchMarketplace(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchProjects/{query}", method = RequestMethod.GET)
    public List<Project> searchProjects(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchProjects(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchMembers/{query}", method = RequestMethod.GET)
    public List<Profile> searchMembers(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchMembers(query, userEPPN);
    }

    @Override
    @RequestMapping(value = "/searchUsers/{query}", method = RequestMethod.GET)
    public List<UserModel> searchUsers(@PathVariable("query") String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchUsers(query, userEPPN);
    }


    @Override
    @RequestMapping(value = "/searchCompanies/{query}", method = RequestMethod.GET)
    public List<Company> searchCompanies(String query, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        return searchImpl.searchCompanies(query, userEPPN);
    }

    @RequestMapping(value = "/triggerFullIndexing/{collectionName}", method = RequestMethod.PATCH)
    public void triggerFullIndexing (@PathVariable("collectionName") String collectionName, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws SearchException {
        SearchQueueImpl.sendFullIndexingMessage(collectionName);
    }
}
