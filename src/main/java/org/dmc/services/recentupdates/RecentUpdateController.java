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
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecentUpdateController {

    private final static String LOGTAG = "ServiceController.class.getName()";
    // private RecentUpdateDao recentUpdateDao = new RecentUpdateDao();

    @RequestMapping(value = "/recent_updates", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity getRecentUpdates(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "getRecentUpdates, userEPPN: " + userEPPN);
        int statusCode = HttpStatus.OK.value();
        String responseText = "a string of response";

        try {
            // companies = companyDao.getCompanies(userEPPN);
            return new ResponseEntity(responseText, HttpStatus.valueOf(statusCode));
        } catch (HTTPException e) {
            return new ResponseEntity(responseText, HttpStatus.valueOf(statusCode));
        }
    }


}
