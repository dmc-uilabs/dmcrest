package org.dmc.services.market;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.components.Component;
import org.dmc.services.products.FavoriteProductsDao;
import org.dmc.services.services.Service;
import org.dmc.services.services.ServiceDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/market", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class MarketController {

    private final String logTag = MarketController.class.getName();

    private ServiceDao serviceDao = new ServiceDao();

    @RequestMapping(value = "/components",
            produces = { "application/json", "text/html" },
            method = RequestMethod.GET)
    public ResponseEntity<List<Component>> marketComponentsGet(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "titleLike", required = false) String titleLike,
            @RequestParam(value = "serviceType", required = false) String serviceType,
            @RequestParam(value = "authors", required = false) List<Integer> authors,
            @RequestParam(value = "ratings", required = false) List<String> ratings,
            @RequestParam(value = "favorites", required = false) String favorites,
            @RequestParam(value = "dates", required = false) List<String> dates,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        // do some magic!
        return new ResponseEntity<List<Component>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/new_services",
            produces = { "application/json", "text/html" },
            method = RequestMethod.GET)
    public ResponseEntity<?> marketNewServicesGet(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            ServiceLogger.log(logTag, "In marketNewServicesGet");
            ArrayList<String> fromLocations = new ArrayList<String>();
            fromLocations.add("marketplace");
            fromLocations.add("project");
            if (null == order && null == sort) {
                order = "DESC";
                sort = "release_date";
            }
            return new ResponseEntity<ArrayList<Service>>(serviceDao.getServices(limit, order, start, sort, null, null, null, null, null, null, null, fromLocations, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/popular_services",
            produces = { "application/json", "text/html" },
            method = RequestMethod.GET)
    public ResponseEntity<?> marketPopularServicesGet(
            @RequestParam(value = "_limit", required = false, defaultValue = "25") Integer limit,
            @RequestParam(value = "_order", required = false, defaultValue = "DESC") String order,
            @RequestParam(value = "_start", required = false, defaultValue = "0") Integer start,
            @RequestParam(value = "_sort", required = false, defaultValue = "id") String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(logTag, "In marketPopularServicesGet: as user " + userEPPN);
        FavoriteProductsDao favoriteProductsDao = new FavoriteProductsDao();

        try {
            return new ResponseEntity<List<Service>>(favoriteProductsDao.getMostPopularProducts(limit, order, start, sort, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services",
            produces = { "application/json", "text/html" },
            method = RequestMethod.GET)
    public ResponseEntity<?> marketServicesGet(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "titleLike", required = false) String titleLike,
            @RequestParam(value = "serviceType", required = false) String serviceType,
            @RequestParam(value = "authors", required = false) List<Integer> authors,
            @RequestParam(value = "ratings", required = false) List<String> ratings,
            @RequestParam(value = "favorites", required = false) String favorites,
            @RequestParam(value = "dates", required = false) List<String> dates,
            @RequestParam(value = "published", required = false) String published,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            ServiceLogger.log(logTag, "In marketServicesGet");
            ArrayList<String> fromLocations = new ArrayList<String>();
            fromLocations.add("marketplace");
            return new ResponseEntity<ArrayList<Service>>(serviceDao.getServices(limit, order, start, sort, titleLike, serviceType, authors, ratings, favorites, dates, published, fromLocations, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}
