package org.dmc.services.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/account-notification-settings", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountNotificationSettingsController {
  

  @RequestMapping(value = "/{notificationSettingID}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<AccountNotificationSetting> accountNotificationSettingsNotificationSettingIDGet(
@PathVariable("notificationSettingID") String notificationSettingID) {
      // do some magic!
      return new ResponseEntity<AccountNotificationSetting>(HttpStatus.NOT_IMPLEMENTED);
  }


 
  @RequestMapping(value = "/{notificationSettingID}", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.PATCH)
  public ResponseEntity<AccountNotificationSetting> accountNotificationSettingsNotificationSettingIDPatch(
@PathVariable("notificationSettingID") String notificationSettingID,
@RequestBody AccountNotificationSetting userNotificationSetting
) {
      // do some magic!
      return new ResponseEntity<AccountNotificationSetting>(HttpStatus.NOT_IMPLEMENTED);
  }

  
}
