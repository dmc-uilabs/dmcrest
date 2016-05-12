package org.dmc.services.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.Company;
import org.dmc.services.services.Service;
import org.dmc.services.users.User;
import org.junit.Assert;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.junit.Test;
import org.junit.Ignore;

import javax.xml.ws.http.HTTPException;
import java.util.List;

/**
 * Created by 200005921 on 2/1/2016.
 */
public class SearchIT {

//    @Test
//    public void testSearchTransformer () {
//
//        String queryString = "component_name : \"Transformer\"";
//
//        //SearchImpl searchImpl = new SearchImpl();
//        SearchController searchController = new SearchController();
//        SearchResult searchResult = null;
//
//        try {
//            searchResult = searchController.search(queryString);
//        } catch (SearchException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(searchResult);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        String jsonString = null;
//        try {
//            jsonString = objectMapper.writeValueAsString(searchResult);
//            System.out.println (jsonString);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }


    public static final String USER_TEST_USER = "testUser";
    public static final String USER_ACME_USER = "acmeUser";
    public static final String USER_JOE_ENGINEER = "joeengineer";

    private final String logTag = SearchIT.class.getName();

    @Test
    public void testSearchAll () {

        String queryString = "transformer";

        //SearchImpl searchImpl = new SearchImpl();
        SearchController searchController = new SearchController();
        SearchResult searchResult = null;

        try {
            searchResult = searchController.search(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            Assert.fail(e.toString());
            e.printStackTrace();
        }

        Assert.assertTrue(searchResult != null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(searchResult);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            Assert.fail(e.toString());
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchProjects () {

        String queryString = "transformer";

        SearchController searchController = new SearchController();
        List<Project> projects = null;
        try {
            projects = searchController.searchProjects(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(projects != null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(projects);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchComponents () {

        String queryString = "transformer";

        SearchController searchController = new SearchController();
        List<Component> components = null;
        try {
            components = searchController.searchComponents(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            e.printStackTrace();
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(components);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(components != null);
        Assert.assertTrue(components.size() > 0);

        Component component = components.get(0);
        Assert.assertTrue(component != null);
        Assert.assertTrue(component.getDescription() != null);
    }

    @Test
    public void testSearchServices () {

        String queryTerm = "demo";
        String queryString = queryTerm + "*";

        SearchController searchController = new SearchController();
        List<Service> services = null;
        try {
            services = searchController.searchServices(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            e.printStackTrace();
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(services);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        int numExpectedServices = 2;
        Assert.assertTrue(services != null);
        Assert.assertTrue("Found " + services.size() + " services; expected " + numExpectedServices, services.size() == numExpectedServices);

        Service service =services.get(0);
        Assert.assertTrue(service != null);
        Assert.assertTrue(service.getDescription() != null);
        Assert.assertTrue("Description '" + service.getDescription() + "' does not match '" + queryString + "'", service.getDescription().toLowerCase().toString().indexOf(queryTerm.toLowerCase()) >= 0);
    }

    @Test
    public void testSearchCompaniesMemberDMDII () {

        String queryString = "GE Global";

        SearchController searchController = new SearchController();
        List<Company> companies = null;
        try {
            companies = searchController.searchCompanies(queryString, USER_JOE_ENGINEER);
        } catch (HTTPException httpEX) {
            Assert.fail(httpEX.getMessage());
        }
        catch (SearchException e) {
            Assert.fail(e.getMessage());
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(companies);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(companies != null);
        Assert.assertTrue(companies.size() > 0);

        Company company  =companies.get(0);
        Assert.assertTrue(company != null);
        Assert.assertTrue(company.getName() != null);
        Assert.assertTrue("Company: " + company.getName() + " does not match: " + queryString, company.getName().toString().toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
    }

    @Test
    public void testSearchCompaniesNonMemberDMDII () {

        String queryString = "GE Global";

        SearchController searchController = new SearchController();
        List<Company> companies = null;
        try {
            companies = searchController.searchCompanies(queryString, USER_ACME_USER);
        } catch (HTTPException httpEX) {
            //Assert.fail(httpEX.getMessage());
            Assert.assertTrue("User " + USER_ACME_USER + " is not DMIDD member, expected response", true);
            return;
        }
        catch (SearchException e) {
            Assert.fail(e.getMessage());
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(companies);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Since non DMDII memeber expected no results returned
        Assert.assertTrue(companies == null);
//        Assert.assertTrue(companies.size() > 0);
//
//        Company company  =companies.get(0);
//        Assert.assertTrue(company != null);
//        Assert.assertTrue(company.getName() != null);
//        Assert.assertTrue("Company: " + company.getName() + " does not match: " + queryString, company.getName().toString().toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
    }

    @Test
    public void testSearchUsers () {

        String queryString = "berlier";

        SearchController searchController = new SearchController();
        List<User> users = null;
        try {
            users = searchController.searchUsers(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            e.printStackTrace();
        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(users);
            ServiceLogger.log(logTag, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(users != null);
        Assert.assertTrue(users.size() > 0);

        User user  = users.get(0);
        Assert.assertTrue(user != null);
        Assert.assertTrue(user.getDisplayName() != null);
        Assert.assertTrue("User: " + user.getDisplayName() + " does not match: " + queryString, user.getDisplayName().toString().toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
    }

    @Test
    public void testSearchUsersCompany () {

        String queryString = "Joe";

        SearchController searchController = new SearchController();
        List<User> users = null;
        try {
            users = searchController.searchUsers(queryString, USER_TEST_USER);
        } catch (SearchException e) {
            e.printStackTrace();
        }

        ServiceLogger.log(logTag, "testSearchUsersCompany: searchUsers returns: " + ((users != null) ? users.size() : 0));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(users);
            ServiceLogger.log(logTag, "testSearchUsersCompany: " + jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(users != null);
        Assert.assertTrue(users.size() > 0);

        int expectedCompanyId = 15;

        User user  = users.get(0);
        Assert.assertTrue(user != null);
        Assert.assertTrue(user.getDisplayName() != null);
        ServiceLogger.log(logTag, "testSearchUsersCompany: user.getDisplayName=" + user.getDisplayName());
        ServiceLogger.log(logTag, "testSearchUsersCompany: user.getCompanyId=" + user.getCompanyId());
        Assert.assertTrue("User: " + user.getDisplayName() + " does not match: " + queryString, user.getDisplayName().toString().toLowerCase().indexOf(queryString.toLowerCase()) >= 0);
        Assert.assertTrue("User: " + user.getDisplayName() + " companyId does not match: " + expectedCompanyId , user.getCompanyId() == expectedCompanyId);
    }

}
