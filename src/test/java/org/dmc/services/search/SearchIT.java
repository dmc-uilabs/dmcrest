package org.dmc.services.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    @Test
    public void testSearchAll () {

        String queryString = "*:*";

        //SearchImpl searchImpl = new SearchImpl();
        SearchController searchController = new SearchController();
        SearchResult searchResult = null;

        try {
            searchResult = searchController.search(queryString);
        } catch (SearchException e) {
            e.printStackTrace();
        }

        System.out.println(searchResult);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(searchResult);

            System.out.println (jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchAllProjects () {

        String queryString = "*:*";

        SearchController searchController = new SearchController();
        List<Project> projects = null;
        try {
            projects = searchController.searchProjects(queryString);
        } catch (SearchException e) {
            e.printStackTrace();
        }

        System.out.println(projects);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(projects);
            System.out.println (jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchAllComponents () {

        String queryString = "*:*";

        SearchController searchController = new SearchController();
        List<Component> components = null;
        try {
            components = searchController.searchComponents(queryString);
        } catch (SearchException e) {
            e.printStackTrace();
        }

        System.out.println(components);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(components);
            System.out.println (jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
