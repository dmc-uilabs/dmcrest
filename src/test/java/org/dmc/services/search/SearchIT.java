package org.dmc.services.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.Service;
import org.junit.Assert;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.junit.Test;

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


    private final String logTag = SearchIT.class.getName();

    @Test
    public void testSearchAll () {

        String queryString = "transformer";

        //SearchImpl searchImpl = new SearchImpl();
        SearchController searchController = new SearchController();
        SearchResult searchResult = null;

        try {
            searchResult = searchController.search(queryString);
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
            projects = searchController.searchProjects(queryString);
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
            components = searchController.searchComponents(queryString);
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

        String queryString = "velocity";

        SearchController searchController = new SearchController();
        List<Service> services = null;
        try {
            services = searchController.searchServices(queryString);
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

        Assert.assertTrue(services != null);
        Assert.assertTrue(services.size() > 0);

        Service service =services.get(0);
        Assert.assertTrue(service != null);
        Assert.assertTrue(service.getDescription() != null);
        Assert.assertTrue(service.getDescription().toString().indexOf("velocity") >= 0);
    }

}
