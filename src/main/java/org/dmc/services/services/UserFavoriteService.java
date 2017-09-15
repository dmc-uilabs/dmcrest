package org.dmc.services.services;

import org.dmc.services.data.entities.UserFavorite;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.userfavorite.FavoriteContentTypeDao;
import org.dmc.services.userfavorite.UserFavoriteDao;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserFavoriteService {

    UserFavoriteDao userFavoriteDao = new UserFavoriteDao();

    public UserFavorite saveUserFavorite(Integer contentId, Integer contentType) {
        final UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserFavorite userFavorite = new UserFavorite();
        FavoriteContentTypeDao favoriteContentTypeDao = new FavoriteContentTypeDao();

        userFavorite.setUserId(user.getId());
        userFavorite.setContentId(contentId);
        userFavorite.setContentType(favoriteContentTypeDao.getFavoriteContentType(contentType));

        return userFavoriteDao.save(userFavorite);

    }

    public Integer deleteUserFavorite(Integer contentId, Integer contentType) {
        final UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserFavorite userFavorite = new UserFavorite();
        FavoriteContentTypeDao favoriteContentTypeDao = new FavoriteContentTypeDao();

        userFavorite.setUserId(user.getId());
        userFavorite.setContentId(contentId);
        userFavorite.setContentType(favoriteContentTypeDao.getFavoriteContentType(contentType));

        return userFavoriteDao.delete(userFavorite);

    }

}
