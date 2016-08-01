package org.dmc.services.utils;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

/**
 * Created by 200005921 on 6/13/2016.
 */
public class SQLUtils {
    public static final Integer DEFAULT_LIMIT = 100;
    public static final String DEFAULT_LIMIT_TEXT = "100";
    public static final String SORT_ASCENDING = "ASC";
    public static final String SORT_DESCENDING = "DESC";

     /**
      * Build an ORDER BY clause:
      * "ORDER BY " + sort + " " + order
     * @param order
     * @param sort
     * @return null or the ORDER BY clause
     */
    public static String buildOrderByClause (String order,  String sort)  {

        String orderByClause = null;
        if (sort != null && sort.trim().length() > 0) {

            orderByClause = "ORDER " + sort.trim();
        }

        // order only added if ORDER BY
        if (orderByClause != null && order != null && order.trim().length() >0) {
            if (order.equals(SORT_ASCENDING) || order.equals(SORT_DESCENDING)) {
                orderByClause += " " + order;
            } else {
                throw new DMCServiceException(DMCError.BadURL, "invalid sort option");
            }
        }

        return orderByClause;
    }

    /**
     * Build a LIMIT clause
     * LIMIT limit
     * @param limit
     * @return null or the LIMIT clause
     */
    public static String buildLimitClause (Integer limit)  {

        String limitClause = null;
        if (limit != null) {
            limitClause = "LIMIT " + limit;
        }
        return limitClause;
    }
}
