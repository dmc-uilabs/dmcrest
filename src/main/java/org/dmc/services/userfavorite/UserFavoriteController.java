package org.dmc.services.userfavorite;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.entities.UserFavorite;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.services.UserFavoriteService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UserFavoriteController {

    private UserFavoriteDao userFavoriteDao;
    private UserFavoriteService userFavoriteService;
    private UserPrincipal user;

    @RequestMapping(value = "/userFavorites", method = RequestMethod.GET)
    public ArrayList<UserFavorite> getUserFavorites(@RequestParam(value = "contentType") Integer contentType) {
        this.user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.userFavoriteDao = new UserFavoriteDao();

        if (contentType != null) {
            return userFavoriteDao.getUserFavorites(user.getId(), contentType);
        } else {
            return userFavoriteDao.getUserFavorites(user.getId(), null);
        }
    }

    @RequestMapping(value = "/userFavorites", method = RequestMethod.POST)
    public UserFavorite postUserFavorite(@RequestParam(value = "contentId") Integer contentId,
                                         @RequestParam(value = "contentType") Integer contentType) throws DMCServiceException {

        ServiceLogger.log("This will be gone soon", contentId.toString());
        this.userFavoriteService = new UserFavoriteService();
        return userFavoriteService.saveUserFavorite(contentId, contentType);

    }

    @RequestMapping(value = "/userFavorites", method = RequestMethod.DELETE)
    public Integer deleteUserFavorite(@RequestParam(value = "contentId") Integer contentId,
                                         @RequestParam(value = "contentType") Integer contentType) throws DMCServiceException {

        this.userFavoriteService = new UserFavoriteService();
        return userFavoriteService.deleteUserFavorite(contentId, contentType);

    }




}
