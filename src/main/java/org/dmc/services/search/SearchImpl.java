package org.dmc.services.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.dmc.services.Config;
import org.dmc.services.ServiceLogger;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.search.handlers.ComponentResponseHandler;
import org.dmc.services.search.handlers.ProjectResponseHandler;
import org.dmc.services.search.handlers.ServiceResponseHandler;
import org.dmc.services.search.handlers.UserResponseHandler;
import org.dmc.services.services.Service;
import org.dmc.services.users.User;
import org.dmc.solr.SolrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 200005921 on 2/1/2016.
 */
public class SearchImpl implements SearchInterface {

    private final String logTag = SearchImpl.class.getName();

    private ComponentResponseHandler componentHandler = new ComponentResponseHandler();
    private ProjectResponseHandler   projectHandler = new ProjectResponseHandler();
    private UserResponseHandler      userHandler = new UserResponseHandler();
    private ServiceResponseHandler   serviceHandler = new ServiceResponseHandler();

    public static final String COLLECTION_COMPONENTS = "gforge_components";
    public static final String COLLECTION_PROJECTS   = "gforge_projects";
    public static final String COLLECTION_SERVICES   = "gforge_services";
    public static final String COLLECTION_USERS      = "gforge_users";
    public static final String COLLECTION_WIKI       = "gforge_wiki";


    private static Map<String, List<String>> fieldMap;


    public SearchImpl () {

        ServiceLogger.log(logTag, "SOLR_BASE_URL: " + SolrUtils.getBaseUrl());
        loadFieldMap();
    }


    protected void loadFieldMap () {

        fieldMap = new TreeMap<String, List<String>>();

        List<String> fieldsComponents = new ArrayList<String>();
        fieldsComponents.add("component_name");
        fieldsComponents.add("group_name");
        fieldsComponents.add("unix_group_name");
        fieldMap.put(COLLECTION_COMPONENTS, fieldsComponents);

        List<String> fieldsProjects = new ArrayList<String>();
        fieldsProjects.add("group_name");
        fieldsProjects.add("unix_group_name");
        fieldMap.put(COLLECTION_PROJECTS, fieldsProjects);

        List<String> fieldsUsers = new ArrayList<String>();
        fieldsUsers.add("user_name");
        fieldsUsers.add("realname");
        fieldMap.put(COLLECTION_USERS, fieldsUsers);

        List<String> fieldsServices = new ArrayList<String>();
        fieldsServices.add("interface_name");
        fieldsServices.add("interface_data");
        fieldsServices.add("group_name");
        fieldsServices.add("unix_group_name");
        fieldMap.put(COLLECTION_SERVICES, fieldsServices);
    }

    protected  List<String> getFieldsForCollection (String collection ) {

        return (fieldMap != null) ? fieldMap.get(collection) : null;

    }

    protected QueryResponse searchSolr (String query, String collection) throws SearchException {

        //HttpSolrServer solrServer = new HttpSolrServer("http://52.35.63.139:8983/solr");
        //SolrClient solrClient = new HttpSolrClient("http://52.35.63.139:8983/solr");

        List<String> fields = getFieldsForCollection (collection);
        ServiceLogger.log(logTag, "fields: " + fields + ", collection: " + collection);
        String solrQuery = SolrUtils.convertToSolrQuery(query, fields);

        SolrClient solrClient = new HttpSolrClient(SolrUtils.getBaseUrl());

        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("q", solrQuery);
        solrParams.set("wt", "json");
        solrParams.set("indent", "true");

        QueryResponse solrQueryResponse = null;
        try {
            ServiceLogger.log(logTag, "solrQuery: " + solrQuery + ", collection: " + collection);
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

        QueryResponse responseUsers = null;
        try {
            responseUsers = searchSolr(query, COLLECTION_USERS);
            List<User> userResults = userHandler.retrieve(responseUsers);
            searchResult.setUsers(userResults);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }

        QueryResponse responseServices = null;
        try {
            responseServices = searchSolr(query, COLLECTION_SERVICES);
            List<Service> serviceResults = serviceHandler.retrieve(responseServices);
            searchResult.setServices(serviceResults);
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
        QueryResponse responseServices = null;
        List<Service> serviceResults = null;
        try {
            responseServices = searchSolr(query, COLLECTION_SERVICES);
            serviceResults = serviceHandler.retrieve(responseServices);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }
        return serviceResults;
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

    @Override
    public List<User> searchUsers(String query) throws SearchException {
        QueryResponse responseUsers = null;
        List<User> userResults = null;
        try {
            responseUsers = searchSolr(query, COLLECTION_USERS);
            userResults = userHandler.retrieve(responseUsers);
        } catch (SearchException e) {
            e.printStackTrace();
            throw new SearchException(e.toString());
        }
        return userResults;
    }
}
