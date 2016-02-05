package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.components.Component;
import org.dmc.services.projects.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class ProjectResponseHandler implements ResponseHandler<Project> {

//    {
//        "short_description": [
//        "Power transformer with improved heat losses relative to a standard iron core."
//        ],
//        "group_name": "Low Heat Loss Transformer",
//            "unix_group_name": "lowheatlosstran",
//            "is_public_b": false,
//            "id": "6",
//            "_version_": 1525156520212299800
//    }

    public static final String FIELD_ID                 = "id";
    public static final String FIELD_SHORT_DESCRIPTION  = "short_description";
    public static final String FIELD_GROUP_NAME         = "group_name";
    public static final String FIELD_UNIX_GROUP_NAME    = "unix_group_name";    public static final String FIELD_COMPONNET_NAME   = "component_name";
    public static final String FIELD_GROUP_ID           = "group_id";


    @Override
    public List<Project> retrieve(QueryResponse queryResponse) {

        List<Project> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String idStr = (String) doc.getFieldValue(FIELD_ID);
                    int id = Integer.parseInt(idStr);
                    //String short_description = (String) doc.getFieldValue(FIELD_SHORT_DESCRIPTION);
                    String group_id = (String) doc.getFieldValue(FIELD_GROUP_ID);
                    String group_name = (String) doc.getFieldValue(FIELD_GROUP_NAME);
                    String unix_group_name = (String) doc.getFieldValue(FIELD_UNIX_GROUP_NAME);

                    Project project =  new Project.ProjectBuilder(id, group_name, null).build();
                    if (l == null) {
                        l = new ArrayList<Project>();
                    }
                    l.add(project);


                }

            }
        }

        return l;
    }

}
