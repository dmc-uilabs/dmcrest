package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.profile.Profile;
import org.dmc.services.users.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class ProfileResponseHandler implements ResponseHandler<Profile> {

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
    public static final String FIELD_TITLE              = "title";
    public static final String FIELD_PHONE              = "phone";
    public static final String FIELD_EMAIL              = "email";
    public static final String FIELD_ADDDRESS           = "address";
    public static final String FIELD_IMAGE              = "image";
    public static final String FIELD_PEOPLE_RESUME      = "people_resume";
    public static final String FIELD_DMDII_EXPIRE_DATE  = "dmdii_expire_date";


    @Override
    public List<Profile> retrieve(QueryResponse queryResponse, String userEPPN) {

        List<Profile> l = null;

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
                    String email = (String) doc.getFieldValue(FIELD_EMAIL);
                    String address = (String) doc.getFieldValue(FIELD_ADDDRESS);
                    String title = (String) doc.getFieldValue(FIELD_TITLE);
                    String phone = (String) doc.getFieldValue(FIELD_PHONE);
                    String image = (String) doc.getFieldValue(FIELD_IMAGE);
                    String people_resume = (String) doc.getFieldValue(FIELD_PEOPLE_RESUME);
                    String dmdii_expire_date = (String) doc.getFieldValue(FIELD_DMDII_EXPIRE_DATE);

                    Profile profile =  new Profile.ProfileBuilder(id, realname, company).build();
                    profile.setEmail(email);
                    profile.setJobTitle(title);
                    profile.setPhone(phone);
                    profile.setImage(image);
                    profile.setLocation(address);
                    profile.setDescription(people_resume);


                    if (l == null) {
                        l = new ArrayList<Profile>();
                    }

                    // Check for expire date > today
                    if (dmdii_expire_date != null && dmdii_expire_date.trim().length() > 0) {
                        l.add(profile);
                    }

                }
            }
        }

        return l;
    }

}
