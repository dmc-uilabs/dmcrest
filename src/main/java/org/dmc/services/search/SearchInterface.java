package org.dmc.services.search;

import org.dmc.services.company.Company;
import org.dmc.services.components.Component;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.profile.Profile;
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
    public SearchResult search (String query, String userEPPN) throws SearchException;

    /**
     * Search components
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Component> searchComponents (String query, String userEPPN) throws SearchException;

    /**
     * Search services
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Service> searchServices (String query, String userEPPN) throws SearchException;

    /**
     * Search projects
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Project> searchProjects (String query, String userEPPN) throws SearchException;

    /**
     * Search users (all users not required to be a DMDII member
     * @param query
     * @return
     * @throws SearchException
     */
    public List<UserModel> searchUsers (String query, String userEPPN) throws SearchException;

    /**
     * Search members searches the users collection but only returns users which are DMDII members
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Profile> searchMembers (String query, String userEPPN) throws SearchException;

    /**
     * Search companies
     * @param query
     * @return
     * @throws SearchException
     */
    public List<Company> searchCompanies (String query, String userEPPN) throws SearchException;
}
