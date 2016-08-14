package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.users.User;

import java.util.ArrayList;
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

	public static final String FIELD_ID = "id";
	public static final String FIELD_REALNAME = "realname";
	public static final String FIELD_USER_NAME = "user_name";
	public static final String FIELD_COMPANY_ID = "company_id";
	public static final String FIELD_COMPANY = "company";

	@Override
	public List<User> retrieve(QueryResponse queryResponse, String userEPPN) {

		List<User> users = new ArrayList<User>();

		if (queryResponse != null) {
			SolrDocumentList documents = queryResponse.getResults();

			for (SolrDocument doc : documents) {
				String idStr = (String) doc.getFieldValue(FIELD_ID);
				int id = Integer.parseInt(idStr);
				String realname = (String) doc.getFieldValue(FIELD_REALNAME);
				String user_name = (String) doc.getFieldValue(FIELD_USER_NAME);
				String companyIdStr = (String) doc.getFieldValue(FIELD_COMPANY_ID);
				int companyId = (companyIdStr != null) ? Integer.parseInt(companyIdStr) : -1;
				users.add(new User(id, user_name, realname, false, companyId));
			}
		}
		return users;
	}
}
