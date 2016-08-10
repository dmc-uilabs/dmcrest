package org.dmc.services.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;
import org.dmc.services.components.Component;
import org.dmc.services.profile.Profile;
import org.dmc.services.projects.Project;
import org.dmc.services.search.handlers.*;
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

    private CompanyResponseHandler companyHandler = new CompanyResponseHandler();
    private ComponentResponseHandler componentHandler = new ComponentResponseHandler();
    private ProjectResponseHandler   projectHandler = new ProjectResponseHandler();
    //private UserResponseHandler      userHandler = new UserResponseHandler();
    private ServiceResponseHandler   serviceHandler = new ServiceResponseHandler();
    private ProfileResponseHandler profileHandler = new ProfileResponseHandler();

    public static final String COLLECTION_COMPANIES  = "gforge_companies";
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
        fieldsUsers.add("company");
        fieldsUsers.add("title");
        fieldsUsers.add("phone");
        fieldsUsers.add("email");
        fieldsUsers.add("address");
        fieldsUsers.add("people_resume");
        fieldMap.put(COLLECTION_USERS, fieldsUsers);

        List<String> fieldsServices = new ArrayList<String>();
        fieldsServices.add(ServiceResponseHandler.FIELD_COMPANY_NAME);
        fieldsServices.add(ServiceResponseHandler.FIELD_COMPANY_DESCRIPTION);
        fieldsServices.add(ServiceResponseHandler.FIELD_OWNER_USER_NAME);
        fieldsServices.add(ServiceResponseHandler.FIELD_OWNER_REALNAME);
        fieldsServices.add(ServiceResponseHandler.FIELD_TITLE);
        fieldsServices.add(ServiceResponseHandler.FIELD_DESCRIPTION);
        fieldsServices.add(ServiceResponseHandler.FIELD_SPECIFICATIONS);
        fieldsServices.add(ServiceResponseHandler.FIELD_TAGS);
        fieldsServices.add(ServiceResponseHandler.FIELD_SERVICE_INTERFACE_NAME);
        fieldMap.put(COLLECTION_SERVICES, fieldsServices);

        List<String> fieldCompanies = new ArrayList<String>();
        fieldCompanies.add(CompanyResponseHandler.FIELD_NAME);
        fieldCompanies.add(CompanyResponseHandler.FIELD_DESCRIPTION);
        fieldCompanies.add(CompanyResponseHandler.FIELD_DIVISION);
        fieldCompanies.add(CompanyResponseHandler.FIELD_INDUSTRY);
        fieldCompanies.add(CompanyResponseHandler.FIELD_LOCATION);
        fieldCompanies.add(CompanyResponseHandler.FIELD_TECH_EXPERTISE);
        fieldCompanies.add(CompanyResponseHandler.FIELD_TOOLS_SOFTWARE_EQUIP_MACH);
        fieldCompanies.add(CompanyResponseHandler.FIELD_COLLABORATION_INTEREST);
        fieldMap.put(COLLECTION_COMPANIES, fieldCompanies);

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
            ServiceLogger.log(logTag, "SolR error searching collection " + collection + ": " + e.toString());
            e.printStackTrace();
            throw new SearchException(e);
        } catch (IOException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + collection + ": " + e.toString());
            throw new SearchException(e);
        }

        return solrQueryResponse;
    }

    @Override
    public SearchResult search(String query, String userEPPN) throws SearchException {
        SearchResult searchResult = new SearchResult();

        QueryResponse responseComponents = null;
        try {
            responseComponents = searchSolr(query, COLLECTION_COMPONENTS);
            List<Component> componentResults = componentHandler.retrieve(responseComponents, userEPPN);
            searchResult.setComponents(componentResults);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_COMPONENTS + ": " + e.toString());
            throw new SearchException(e.toString());
        }

        QueryResponse responseProjects = null;
        try {
            responseProjects = searchSolr(query, COLLECTION_PROJECTS);
            List<Project> projectResults = projectHandler.retrieve(responseProjects, userEPPN);
            searchResult.setProjects(projectResults);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_PROJECTS + ": " + e.toString());
            throw new SearchException(e.toString());
        }

//        QueryResponse responseUsers = null;
//        try {
//            responseUsers = searchSolr(query, COLLECTION_USERS);
//            List<User> userResults = userHandler.retrieve(responseUsers, userEPPN);
//            searchResult.setUsers(userResults);
//        } catch (SearchException e) {
//            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_USERS + ": " + e.toString());
//            throw new SearchException(e.toString());
//        }

        QueryResponse responseUsers = null;
        try {
            responseUsers = searchSolr(query, COLLECTION_USERS);
            List<Profile> profileResults = profileHandler.retrieve(responseUsers, userEPPN);
            searchResult.setProfiles(profileResults);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_USERS + ": " + e.toString());
            throw new SearchException(e.toString());
        }
        QueryResponse responseServices = null;
        try {
            responseServices = searchSolr(query, COLLECTION_SERVICES);
            List<Service> serviceResults = serviceHandler.retrieve(responseServices, userEPPN);
            searchResult.setServices(serviceResults);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_SERVICES + ": " + e.toString());
            throw new SearchException(e.toString());
        }

        QueryResponse responseCompanies = null;
        try {
            responseCompanies = searchSolr(query, COLLECTION_COMPANIES);
            List<Company> companyResults = companyHandler.retrieve(responseCompanies, userEPPN);
            searchResult.setCompanies(companyResults);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_COMPANIES + ": " + e.toString());
            throw new SearchException(e.toString());
        }

        return searchResult;
    }

    @Override
    public List<Component> searchComponents(String query, String userEPPN) throws SearchException {
        QueryResponse responseComponents = null;
        List<Component> componentResults = null;
        try {
            responseComponents = searchSolr(query, COLLECTION_COMPONENTS);
            componentResults = componentHandler.retrieve(responseComponents, userEPPN);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_COMPONENTS + ": " + e.toString());
            throw new SearchException(e.toString());
        }
        return componentResults;
    }

    @Override
    public List<Service> searchServices(String query, String userEPPN) throws SearchException {
        QueryResponse responseServices = null;
        List<Service> serviceResults = null;
        try {
            responseServices = searchSolr(query, COLLECTION_SERVICES);
            serviceResults = serviceHandler.retrieve(responseServices, userEPPN);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_SERVICES + ": " + e.toString());
            throw new SearchException(e.toString());
        }
        return serviceResults;
    }

    @Override
    public List<Project> searchProjects(String query, String userEPPN) throws SearchException {
        QueryResponse responseProjects = null;
        List<Project> projectResults = null;
        try {
            responseProjects = searchSolr(query, COLLECTION_PROJECTS);
            projectResults = projectHandler.retrieve(responseProjects, userEPPN);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_PROJECTS + ": " + e.toString());
            throw new SearchException(e.toString());
        }
        return projectResults;
    }

//    @Override
//    public List<User> searchUsers(String query, String userEPPN) throws SearchException {
//        QueryResponse responseUsers = null;
//        List<User> userResults = null;
//        try {
//            responseUsers = searchSolr(query, COLLECTION_USERS);
//            userResults = userHandler.retrieve(responseUsers, userEPPN);
//        } catch (SearchException e) {
//            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_USERS + ": " + e.toString());
//            throw new SearchException(e.toString());
//        }
//        return userResults;
//    }

    @Override
    public List<Profile> searchMembers(String query, String userEPPN) throws SearchException {
        QueryResponse responseUsers = null;
        List<User> userResults = null;
        List<Profile> profileResults = null;
        try {
            responseUsers = searchSolr(query, COLLECTION_USERS);
            profileResults = profileHandler.retrieve(responseUsers, userEPPN);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_USERS + ": " + e.toString());
            throw new SearchException(e.toString());
        }

        return profileResults;
    }

    @Override
    public List<Company> searchCompanies(String query, String userEPPN) throws SearchException {
        QueryResponse responseCompanies = null;
        List<Company> companyResults = null;
        try {
            responseCompanies = searchSolr(query, COLLECTION_COMPANIES);
            companyResults = companyHandler.retrieve(responseCompanies, userEPPN);
        } catch (SearchException e) {
            ServiceLogger.log(logTag, "SolR error searching collection " + COLLECTION_COMPANIES + ": " + e.toString());
            throw new SearchException(e.toString());
        }
        return companyResults;    }
}
