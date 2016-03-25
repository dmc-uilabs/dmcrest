package org.dmc.services.search.handlers;

import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;

/**
 * Created by 200005921 on 2/2/2016.
 */
public interface ResponseHandler<T> {

    public abstract List<T> retrieve(QueryResponse queryResponse, String userEPPN);
}
