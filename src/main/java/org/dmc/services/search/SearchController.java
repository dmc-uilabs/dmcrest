package org.dmc.services.search;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.search.handlers.ComponentResponseHandler;
import org.dmc.services.search.handlers.ResponseHandler;
import org.dmc.services.services.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SearchResult search (@PathVariable("query")String query) throws SearchException {
        return searchImpl.search(query);
    }

    @Override
    @RequestMapping(value = "/searchComponents", method = RequestMethod.GET)
    public List<Component> searchComponents(@PathVariable("query") String query) throws SearchException {
        return searchImpl.searchComponents(query);
    }

    @Override
    @RequestMapping(value = "/searchServices", method = RequestMethod.GET)
    public List<Service> searchServices(@PathVariable("query") String query) throws SearchException {
        return searchImpl.searchServices(query);
    }

    @Override
    @RequestMapping(value = "/searchProjects", method = RequestMethod.GET)
    public List<Project> searchProjects(@PathVariable("query") String query) throws SearchException {
        return searchImpl.searchProjects(query);
    }
}
