package org.dmc.services.search;

import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;
import org.dmc.services.services.Service;
import org.dmc.services.users.User;

import java.util.List;

/**
 * Created by 200005921 on 2/3/2016.
 */
public interface SearchInterface {

    /**
     * Search all collections
     * @param query
     * @return
     * @throws SearchException
     */
    public SearchResult search (String query) throws SearchException;

    /**
     * Search components
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Component> searchComponents (String query) throws SearchException;

    /**
     * Search services
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Service> searchServices (String query) throws SearchException;

    /**
     * Search projects
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Project> searchProjects (String query) throws SearchException;

    /**
     * Search users
     * @param query
     * @return
     * @throws SearchException
     */
    public List<User> searchUsers (String query) throws SearchException;
}
