package org.dmc.solr;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.dmc.services.Config;
import org.dmc.services.ServiceLogger;
import org.dmc.services.search.SearchException;
import org.dmc.services.search.SearchQueueImpl;

/**
 * Created by 200005921 on 1/13/2016.
 */
public class SolrUtils {

	private static final String logTag = SolrUtils.class.getName();


    //public static String BASE_URL = "http://52.24.49.48:8983/solr/";
    public static String FULL_IMPORT = "dataimport?command=full-import&clean=true";

    public static final String CORE_GFORGE_COMPANIES  = "gforge_companies";
    public static final String CORE_GFORGE_COMPONENTS = "gforge_components";
    public static final String CORE_GFORGE_PROJECTS   = "gforge_projects";
    public static final String CORE_GFORGE_SERVICES   = "gforge_services";
    public static final String CORE_GFORGE_USERS      = "gforge_users";
    public static final String CORE_GFORGE_WIKI       = "gforge_wiki";

    public static final String Q_ALL_FIELDS = "*";

    // http://52.88.250.74:8983/solr/gforge_users/dataimport?command=full-import&clean=true

    private static String baseUrl;
    static
    {
        baseUrl = System.getenv("SOLR_BASE_URL");
    }


	public static String getBaseUrl () {
		return baseUrl;
	}

    public static String invokeFulIndexingComponents () throws IOException {
        return invokeFullIndexing(baseUrl, CORE_GFORGE_COMPONENTS);
    }

    public static String invokeFulIndexingProjects () throws IOException {
        return invokeFullIndexing(baseUrl, CORE_GFORGE_PROJECTS);
    }

    public static String invokeFulIndexingServices () throws IOException {
        return invokeFullIndexing(baseUrl, CORE_GFORGE_SERVICES);
    }

    public static String invokeFulIndexingUsers () throws IOException {
        return invokeFullIndexing(baseUrl, CORE_GFORGE_USERS);
    }

    public static String invokeFulIndexingWiki () throws IOException {
        return invokeFullIndexing(baseUrl, CORE_GFORGE_WIKI);
    }

    public static String invokeFullIndexing (String baseUrl, String coreName) throws IOException {

        String url = baseUrl + coreName + "/" + FULL_IMPORT;

        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;

        httpClient = HttpClients.createDefault();

        ServiceLogger.log(logTag, "Triggering SOLR: " + url);

        HttpGet httpGet = new HttpGet(url);
        String responseText = null;

        try {
            response = httpClient.execute(httpGet);
            responseText = getResponseContent(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }

        return responseText;
    }


    public static String getResponseContent (HttpEntity entity) throws IOException {

        String response = null;

        if (entity != null) {

            InputStreamReader isr = null;
            BufferedReader br = null;
            try {

                isr = new InputStreamReader(entity.getContent());
                br = new BufferedReader(isr);
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(line);
                }

                response = out.toString();

            } catch (IOException ioE) {
                throw new IOException("Error reading response: " + ioE.toString());
            } finally {
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e) {

                    }
                    isr = null;
                }
            }

        }

        return response;
    }

    public static String convertToSolrQuery (String query, List<String> fields) {
        String solrQuery = query;
        if (query != null) {

            if (fields != null) {
                solrQuery = "";
                for (int i=0; i < fields.size(); i++) {

                    if (i > 0) solrQuery += " OR ";

                    //String Q = fields.get(i) + ":" + "\"" + query + "\"";
                    String Q = fields.get(i) + ":" + query;
                    solrQuery += Q;
                }
            } else {

            }
        }
        else {
            solrQuery = "*:*";
        }
        return solrQuery;

    }

    /**
     *
     * Trigger full indexing of solr by inserting a message into the SEARCH_QUEUE
     * Call this function in DAOs whenever data is updated
     *
     * @param solrCoreName Specifies the name of the core to re-index
     *                     SolrUtils.CORE_GFORGE_COMPANIES
     *                     SolrUtils.CORE_GFORGE_COMPONENTS
     *                     SolrUtils.CORE_GFORGE_PROJECTS
     *                     SolrUtils.CORE_GFORGE_SERVICES
     *                     SolrUtils.CORE_GFORGE_USERS
     *                     SolrUtils.CORE_GFORGE_WIKI
     *
     */
    public static void triggerFullIndexing (String solrCoreName) throws SearchException {
        if (Config.IS_TEST == null) {
            //ServiceLogger.log(LOGTAG, "SolR indexing turned off");
            // Trigger solr indexing
            try {
                SearchQueueImpl.sendFullIndexingMessage(solrCoreName);
                ServiceLogger.log(logTag, "SolR indexing triggered for SOLR core: " + solrCoreName);
            } catch (SearchException e) {
                throw e;
            }
        }
    }
}
