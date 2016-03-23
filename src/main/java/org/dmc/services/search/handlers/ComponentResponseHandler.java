package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.components.Component;
import org.dmc.services.components.ComponentDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class ComponentResponseHandler implements ResponseHandler<Component> {

//    <document>
//    <entity name="component"
//    query="select cem_id as id, name, group_id from cem_objects">
//    <field column="id" name="id" />
//    <field column="name" name="component_name" />
//    <field column="group_id" name="group_id" />
//    <entity name="groups"
//    query="select group_name, unix_group_name, is_public from groups where group_id = '${component.group_id}'">
//    <field column="unix_group_name" name="unix_group_name" />
//    <field column="group_name" name="group_name" />
//    <field column="is_public" name="is_public" />
//    </entity>
//    <entity name="cem_service_categories"
//    query="select cat_id from cem_service_categories where cem_id = '${component.id}'">
//    <entity name="category"
//    query="select name as category_name from service_categories where cat_id = '${cem_service_categories.cat_id}'">
//    <field column="category_name" name="category_name"/>
//    </entity>
//    </entity>
//    </entity>
//    </document>

    public static final String FIELD_ID               = "id";
    public static final String FIELD_COMPONNET_NAME   = "component_name";
    public static final String FIELD_GROUP_ID         = "group_id";
    public static final String FIELD_GROUP_NAME       = "group_name";
    public static final String FIELD_UNIX_GROUP_NAME  = "unix_group_name";

    private ComponentDao componentDao = new ComponentDao();

    @Override
    public List<Component> retrieve(QueryResponse queryResponse, String userEPPN) {

        List<Component> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String component_id = (String) doc.getFieldValue(FIELD_ID);
                    int comp_id = Integer.parseInt(component_id);
//                    String component_name = (String) doc.getFieldValue(FIELD_COMPONNET_NAME);
//                    String component_group_id = (String) doc.getFieldValue(FIELD_GROUP_ID);
//                    String group_name = (String) doc.getFieldValue(FIELD_GROUP_NAME);
//                    String unix_group_name = (String) doc.getFieldValue(FIELD_UNIX_GROUP_NAME);
//                    String component_description = "";
//                    Component component =  new Component.ComponentBuilder(comp_id, component_name, component_description).build();

                    Component component = componentDao.getComponent(comp_id);
                    if (l == null) {
                        l = new ArrayList<Component>();
                    }
                    l.add(component);

                }

            }
        }

        return l;
    }

}
