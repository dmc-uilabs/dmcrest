package org.dmc.services.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.dmc.services.ServiceLogger;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.search.handlers.ComponentResponseHandler;
import org.dmc.services.search.handlers.ProjectResponseHandler;
import org.dmc.services.services.Service;
import org.dmc.solr.SolrUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by 200005921 on 2/1/2016.
 */
public class SearchImpl implements SearchInterface {

    private final String logTag = SearchImpl.class.getName();

    private ComponentResponseHandler componentHandler = new ComponentResponseHandler();
    private ProjectResponseHandler   projectHandler = new ProjectResponseHandler();

    public static final String COLLECTION_COMPONENTS = "gforge_components";
    public static final String COLLECTION_PROJECTS   = "gforge_projects";

    protected QueryResponse searchSolr (String query, String collection) throws SearchException {

        //HttpSolrServer solrServer = new HttpSolrServer("http://52.35.63.139:8983/solr");
        //SolrClient solrClient = new HttpSolrClient("http://52.35.63.139:8983/solr");

        SolrClient solrClient = new HttpSolrClient(SolrUtils.getBaseUrl());

        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("q", query);
        solrParams.set("wt", "json");
        solrParams.set("indent", "true");

        QueryResponse solrQueryResponse = null;
        try {
            solrQueryResponse = solrClient.query(collection, solrParams);

            ServiceLogger.log(logTag, "solrQueryResponse: " + solrQueryResponse);

            // solrQueryResponse.getResults()

        } catch (SolrServerException e) {
            e.printStackTrace();
            throw new SearchException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SearchException(e);
        }

        return solrQueryResponse;
    }

    @Override
    public SearchResult search(String query) throws SearchException {
        SearchResult searchResult = new SearchResult();

        QueryResponse responseComponents = null;
        try {
            responseComponents = searchSolr(query, COLLECTION_COMPONENTS);
            List<Component> componentResults = componentHandler.retrieve(responseComponents);
            searchResult.setComponents(componentResults);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }

        QueryResponse responseProjects = null;
        try {
            responseProjects = searchSolr(query, COLLECTION_PROJECTS);
            List<Project> projectResults = projectHandler.retrieve(responseProjects);
            searchResult.setProjects(projectResults);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }
        return searchResult;
    }

    @Override
    public List<Component> searchComponents(String query) throws SearchException {
        QueryResponse responseComponents = null;
        List<Component> componentResults = null;
        try {
            responseComponents = searchSolr(query, COLLECTION_COMPONENTS);
            componentResults = componentHandler.retrieve(responseComponents);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }
        return componentResults;
    }

    @Override
    public List<Service> searchServices(String query) throws SearchException {
        return null;
    }

    @Override
    public List<Project> searchProjects(String query) throws SearchException {
        QueryResponse responseProjects = null;
        List<Project> projectResults = null;
        try {
            responseProjects = searchSolr(query, COLLECTION_PROJECTS);
            projectResults = projectHandler.retrieve(responseProjects);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }
        return projectResults;
    }
}
