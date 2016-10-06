package org.dmc.services.products;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/favorite_products", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FavoriteProductsController {
    private static final String LOGTAG = FavoriteProductsController.class.getName();
    
    @RequestMapping(value = "/{favorite_productId}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
    public ResponseEntity<?> favoriteProductsFavoriteProductIdDelete(@PathVariable("favoriteProductId") Integer favoriteProductId,
                                                                     @RequestHeader(value="AJP_eppn", required=true) String userEPPN){
        ServiceLogger.log(LOGTAG, "In favoriteProductsFavoriteProductIdDelete: for favoriteProductId " + favoriteProductId + " as user " + userEPPN);
        FavoriteProductsDao favoriteProductsDao = new FavoriteProductsDao();
        
        try {
            favoriteProductsDao.deleteFavoriteProduct(favoriteProductId, userEPPN);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    
    
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE },method = RequestMethod.POST)
    public ResponseEntity<?> favoriteProductsPost(@RequestParam(value = "accountId", required = true) Integer accountId,
                                                  @RequestParam(value = "serviceId", required = true) Integer serviceId,
                                                  @RequestHeader(value="AJP_eppn", required=true) String userEPPN){
        ServiceLogger.log(LOGTAG, "In favoriteProductsPost: for accountID " + accountId +
                          " and serviceID " + serviceId + " as user " + userEPPN);
        FavoriteProductsDao favoriteProductsDao = new FavoriteProductsDao();
        
        try {
            favoriteProductsDao.createFavoriteProduct(accountId, serviceId, userEPPN);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    /*
     Needs to support:
     /favorite_products?serviceId=1&serviceId=2
     /favorite_products?accountId=1005
     */
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE },method = RequestMethod.GET)
    public ResponseEntity<?> favoriteProductsGet(@RequestParam(value = "accountId", required = false) List<Integer> accountIds,
                                                 @RequestParam(value = "serviceId", required = false) List<Integer> serviceIds,
                                                 @RequestHeader(value="AJP_eppn", required=true) String userEPPN){
        ServiceLogger.log(LOGTAG, "In favoriteProductsGet:  as user " + userEPPN);
        FavoriteProductsDao favoriteProductsDao = new FavoriteProductsDao();
        
        try {
            if(accountIds.size() != 0) {
                return new ResponseEntity<List<FavoriteProduct>>(favoriteProductsDao.getFavoriteProductForAccounts(accountIds, userEPPN), HttpStatus.OK);
            } else if(serviceIds.size() != 0) {
                return new ResponseEntity<List<FavoriteProduct>>(favoriteProductsDao.getFavoriteProductForServices(serviceIds, userEPPN), HttpStatus.OK);
            } else {
                throw new DMCServiceException(DMCError.IncorrectType, "unknown Request parameter");
            }
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}

