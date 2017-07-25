package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
//import org.dmc.services.ServiceLogger;
//import org.dmc.services.search.SearchImpl;
import org.dmc.services.services.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class ServiceResponseHandler implements ResponseHandler<Service> {

    private final String logTag = ServiceResponseHandler.class.getName();
//    "docs": [
//    {
//        "parent": "parent 1",
//            "project_id": "1",
//            "owner_id": "102",
//            "organization_id": "1",
//            "description": "this is a demo service",
//            "id": "1",
//            "published": "true",
//            "title": "demo service",
//            "type": "type 1",
//            "specifications": "specification1",
//            "tags": "tag1",
//            "company_name": "General Electric",
//            "owner_user_name": "fforgeadmin",
//            "owner_realname": "Forge Admin",
//            "_version_": 1533878078556602400
//    },
//    // id, user_name, realname, skills_data.skill_keyword

    public static final String FIELD_ID                  = "id";
    public static final String FIELD_ORGANIZATION_ID     = "organization_id";
    public static final String FIELD_TITLE               = "title";
    public static final String FIELD_DESCRIPTION         = "description";
    public static final String FIELD_OWNER_ID            = "owner_id";
    public static final String FIELD_TAGS                = "tags";
    public static final String FIELD_SPECIFICATIONS      = "specifications";
    public static final String FIELD_PROJECT_ID          = "project_id";
    public static final String FIELD_TYPE                = "type";
    public static final String FIELD_PUBLISHED           = "published";
    public static final String FIELD_RELEASE_DATE        = "release_date";
    public static final String FIELD_SERVICE_TYPE        = "service_type";
    public static final String FIELD_FROM_LOCATION       = "from_location";
    public static final String FIELD_COMPANY_NAME        = "company_name";
    public static final String FIELD_COMPANY_DESCRIPTION = "company_description";
    public static final String FIELD_OWNER_USER_NAME     = "owner_user_name";
    public static final String FIELD_OWNER_REALNAME      = "owner_realname";
    public static final String FIELD_SERVICE_INTERFACE_NAME      = "service_interface_name";



    @Override
    public List<Service> retrieve(QueryResponse queryResponse, String userEPPN) {

        List<Service> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String idStr = (String) doc.getFieldValue(FIELD_ID);
                    int id = Integer.parseInt(idStr);

                    String organizationIdStr = (String) doc.getFieldValue(FIELD_ORGANIZATION_ID);
                    String title = (String) doc.getFieldValue(FIELD_TITLE);
                    String description = (String) doc.getFieldValue(FIELD_DESCRIPTION);
                    String ownerIdStr = (String) doc.getFieldValue(FIELD_OWNER_ID);
                    String specifications = (String) doc.getFieldValue(FIELD_SPECIFICATIONS);
                    String projectIdStr = (String) doc.getFieldValue(FIELD_PROJECT_ID);
                    String type = (String) doc.getFieldValue(FIELD_TYPE);
                    String published = (String) doc.getFieldValue(FIELD_PUBLISHED);
                    String release_date = (String) doc.getFieldValue(FIELD_RELEASE_DATE);
                    String from_location = (String) doc.getFieldValue(FIELD_FROM_LOCATION);
                    String service_type = (String) doc.getFieldValue(FIELD_SERVICE_TYPE);
                    List<String> tags = (List<String>) doc.getFieldValue(FIELD_TAGS);

                    Service service =  new Service();
          					service.setId(id);
          					service.setTitle(title);
          					service.setDescription(description);
                    service.setOwner(ownerIdStr);
                    service.setType(type);
                    service.setCompanyId(organizationIdStr);
                    service.setProfileId(ownerIdStr);
                    service.setProjectId(projectIdStr);
                    service.setPublished(Boolean.valueOf(published));
                    try {
                      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                      service.setReleaseDate(format.parse(release_date));
                    }
                    catch (Exception e) {
                      // Date could not be formatted
                    }
                    service.setFrom(from_location);
                    service.setServiceType(service_type);
                    service.setSpecifications(specifications);
                    service.setTags(tags);

                    if (l == null) {
                        l = new ArrayList<Service>();
                    }
                    l.add(service);
                }
            }
        }

        return l;
    }

}
