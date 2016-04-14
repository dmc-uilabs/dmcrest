package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.users.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class UserResponseHandler implements ResponseHandler<User> {

//    {
//        "user_name": "berlier",
//            "id": "103",
//            "realname": "berlier test",
//            "_version_": 1525634045174612000
//    },

    // id, user_name, realname, skills_data.skill_keyword

    public static final String FIELD_ID                 = "id";
    public static final String FIELD_REALNAME           = "realname";
    public static final String FIELD_USER_NAME          = "user_name";
    public static final String FIELD_COMPANY_ID         = "company_id";
    public static final String FIELD_COMPANY            = "company";


    @Override
    public List<User> retrieve(QueryResponse queryResponse, String userEPPN) {

        List<User> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String idStr = (String) doc.getFieldValue(FIELD_ID);
                    int id = Integer.parseInt(idStr);
                    String realname = (String) doc.getFieldValue(FIELD_REALNAME);
                    String user_name = (String) doc.getFieldValue(FIELD_USER_NAME);
                    String companyIdStr = (String) doc.getFieldValue(FIELD_COMPANY_ID);
                    int companyId = (companyIdStr != null) ? Integer.parseInt(companyIdStr) : -1;
                    String company = (String) doc.getFieldValue(FIELD_COMPANY);

                    User user =  new User.UserBuilder(id, user_name, realname, companyId).build();

                    if (l == null) {
                        l = new ArrayList<User>();
                    }
                    l.add(user);
                }
            }
        }

        return l;
    }

}
