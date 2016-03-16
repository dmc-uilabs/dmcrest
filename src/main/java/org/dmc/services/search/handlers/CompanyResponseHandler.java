package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dmc.services.company.Company;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.users.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public class CompanyResponseHandler implements ResponseHandler<Company> {

    public static final String FIELD_ID                          = "id";
    public static final String FIELD_NAME                        = "name";
    public static final String FIELD_LOCATION                    = "location";
    public static final String FIELD_DIVISION                    = "division";
    public static final String FIELD_DESCRIPTION                 = "description";
    public static final String FIELD_INDUSTRY                    = "industry";
    public static final String FIELD_TECH_EXPERTISE              = "tech_expertise";
    public static final String FIELD_TOOLS_SOFTWARE_EQUIP_MACH   = "tools_software_equip_mach";
    public static final String FIELD_COLLABORATION_INTEREST      = "collaboration_interest";

    private CompanyDao companyDao = new CompanyDao();

    @Override
    public List<Company> retrieve(QueryResponse queryResponse, String userEPPN) {

        List<Company> l = null;

        if (queryResponse != null) {

            SolrDocumentList documents = queryResponse.getResults();
            if (documents != null) {

                Iterator<SolrDocument> docIter = documents.iterator();
                while (docIter.hasNext()) {
                    SolrDocument doc = docIter.next();

                    String idStr = (String) doc.getFieldValue(FIELD_ID);
                    int id = Integer.parseInt(idStr);
                    String name = (String) doc.getFieldValue(FIELD_NAME);
//                    String location = (String) doc.getFieldValue(FIELD_LOCATION);
//                    String division = (String) doc.getFieldValue(FIELD_DIVISION);
//                    String description = (String) doc.getFieldValue(FIELD_DESCRIPTION);
//                    String industry = (String) doc.getFieldValue(FIELD_INDUSTRY);
//                    String tech_expertise = (String) doc.getFieldValue(FIELD_TECH_EXPERTISE);
//                    String tools_software_equip_mach = (String) doc.getFieldValue(FIELD_TOOLS_SOFTWARE_EQUIP_MACH);
//                    String collaboration_interest = (String) doc.getFieldValue(FIELD_COLLABORATION_INTEREST);

                    Company company = companyDao.getCompany(id, userEPPN);
                    if (l == null) {
                        l = new ArrayList<Company>();
                    }
                    l.add(company);
                }
            }
        }

        return l;
    }

}
