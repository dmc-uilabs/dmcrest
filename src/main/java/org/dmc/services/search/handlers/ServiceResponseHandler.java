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

/**
 * Created by 200005921 on 2/2/2016.
 */
public class ServiceResponseHandler implements ResponseHandler<Service> {

    private final String logTag = ServiceResponseHandler.class.getName();
//    {
//        "cem_id": "2",
//            "id": "1",
//            "interface_data": "{\"interFace\":{\"version\":1,\"modelId\":\"995f865e-d90a-1004-8438-64281c6cab63\",\"interfaceId\":\"995f8660-d90a-1004-8438-64281c6cab63\",\"type\":\"interface\",\"name\":\"velocity interface\",\"path\":[33,34,35]},\"inParams\":{\"timeCopy\":{\"name\":\"timeCopy\",\"type\":\"Real\",\"unit\":\"second\",\"category\":\"time\",\"value\":2,\"parameterid\":\"0163d124-d8de-1004-8a2f-592d01a9bb93\"},\"distanceCopy\":{\"name\":\"distanceCopy\",\"type\":\"Real\",\"unit\":\"centimeter\",\"category\":\"length\",\"value\":3,\"parameterid\":\"0163d123-d8de-1004-8a2f-592d01a9bb93\"}},\"outParams\":{\"averageVelocity\":{\"name\":\"averageVelocity\",\"type\":\"Real\",\"unit\":\"centimeter per second\",\"category\":\"velocity\",\"value\":1.5,\"parameterid\":\"0163d125-d8de-1004-8a2f-592d01a9bb93\",\"instancename\":\"averageVelocity\"}},\"modelName\":\"velocity interface\",\"modelDescription\":\"\",\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}}",
//            "server_id": "1",
//            "interface_name": "velocity interface",
//            "user_id": "102",
//            "server_url": "http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/",
//            "group_id": "6",
//            "group_name": "Low Heat Loss Transformer",
//            "is_public": false,
//            "unix_group_name": "lowheatlosstran",
//            "_version_": 1525642535312031700
//    }

    // id, user_name, realname, skills_data.skill_keyword

    public static final String FIELD_ID                 = "id";
    public static final String FIELD_CEM_ID             = "cem_id";
    public static final String FIELD_INTERFACE_DATA     = "interface_data";
    public static final String FIELD_SERVER_ID          = "server_id";
    public static final String FIELD_INTERFACE_NAME     = "interface_name";
    public static final String FIELD_USER_ID            = "user_id";
    public static final String FIELD_SERVER_URL         = "server_url";
    public static final String FIELD_GROUP_ID           = "group_id";
    public static final String FIELD_GROUP_NAME         = "group_name";
    public static final String FIELD_IS_PUBLIC          = "is_public";
    public static final String FIELD_UNIX_GROUP_NAME    = "unix_group_name";
    public static final String FIELD_TITLE              = "title";
    public static final String FIELD_DESCRIPTION        = "description";


    @Override
    public List<Service> retrieve(QueryResponse queryResponse) {

        List<Service> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String idStr = (String) doc.getFieldValue(FIELD_ID);
                    int id = Integer.parseInt(idStr);
                    
                    String interfaceData = (String) doc.getFieldValue(FIELD_INTERFACE_DATA);
                    String interfaceName = (String) doc.getFieldValue(FIELD_INTERFACE_NAME);
                    String serverUrl = (String) doc.getFieldValue(FIELD_SERVER_URL);
                    String groupName = (String) doc.getFieldValue(FIELD_GROUP_NAME);
                    String unixGroupName = (String) doc.getFieldValue(FIELD_UNIX_GROUP_NAME);
                    String title = (String) doc.getFieldValue(FIELD_TITLE);
                    String description = (String) doc.getFieldValue(FIELD_DESCRIPTION);

//                    ServiceLogger.log(logTag, "doc: " + doc);
//                    ServiceLogger.log(logTag, "->id: " + id);
//                    ServiceLogger.log(logTag, "->interfaceData: " + interfaceData);
//                    ServiceLogger.log(logTag, "->interfaceName: " + interfaceName);
//                    ServiceLogger.log(logTag, "->serverUrl: " + serverUrl);
//                    ServiceLogger.log(logTag, "->groupName: " + groupName);
//                    ServiceLogger.log(logTag, "->unixGroupName: " + unixGroupName);
//                    ServiceLogger.log(logTag, "->title: " + title);
//                    ServiceLogger.log(logTag, "->description: " + description);

                    Service service =  new Service.ServiceBuilder(id, title, description).build();
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
