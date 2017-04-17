package org.dmc.services.recentupdates;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.dmc.services.data.entities.DMDIIDocument;

@RestController
public class RecentUpdateController {

    private final static String LOGTAG = "ServiceController.class.getName()";
    private RecentUpdateDao recentUpdateDao = new RecentUpdateDao();

    @RequestMapping(value = "/recent_updates", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE})
    @PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
    public ResponseEntity getRecentUpdates(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
        @RequestParam(value = "limit", required = false, defaultValue = "3") Integer limit) {
        ServiceLogger.log(LOGTAG, "getRecentUpdates, userEPPN: " + userEPPN);
        int statusCode = HttpStatus.OK.value();

        try {
          ArrayList<RecentUpdate> recentUpdates = null;
          recentUpdates = recentUpdateDao.getRecentUpdates(userEPPN, limit);
          return new ResponseEntity<ArrayList<RecentUpdate>>(recentUpdates, HttpStatus.valueOf(statusCode));
        } catch (HTTPException e) {
          return new ResponseEntity(e, HttpStatus.valueOf(statusCode));
        }
    }

    public void addRecentUpdate(Object updateEntity) {
      recentUpdateDao.createRecentUpdate(updateEntity);
    }

    public void addRecentUpdate(Object updatedItem, Object originalItem) {
      recentUpdateDao.createRecentUpdate(updatedItem, originalItem);
    }

}
