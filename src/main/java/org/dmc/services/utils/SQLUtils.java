package org.dmc.services.utils;

import java.util.List;

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
    public static String buildOrderByClause (String order,  String sort, List<String> validSortFields)  {

        String orderByClause = null;
        boolean lastFieldHasOrder = false;
        if (sort != null && sort.trim().length() > 0) {
            orderByClause = "ORDER ";
            final String[] tokens = sort.split(",");
            for (String token : tokens) {
                final String[] parts = token.trim().split(" +");
                if (parts.length > 2) {
                    throw new DMCServiceException(DMCError.BadURL, "invalid sort field: " + token.trim() + " parts.length=" + parts.length);
                }
                if (!validSortFields.contains(parts[0].trim())) {
                    throw new DMCServiceException(DMCError.BadURL, "invalid sort field: " + token.trim());
                }
                orderByClause += parts[0];
                if (parts.length > 1) {
                    if (parts[1].equals(SORT_ASCENDING) || parts[1].equals(SORT_DESCENDING)) {
                        orderByClause += " " + parts[1];
                        lastFieldHasOrder = true;
                    } else {
                        throw new DMCServiceException(DMCError.BadURL, "invalid sort option " + parts[1] + " on field " + parts[0]);
                    }
                } else {
                    lastFieldHasOrder = false;
                }
                orderByClause += ", ";
            }
        }

        if (orderByClause != null) {
            orderByClause = orderByClause.substring(0, orderByClause.length()-2);   // remove last ", "
        }
        // order only added if ORDER BY
        if (orderByClause != null && order != null && order.trim().length() >0) {
            if (lastFieldHasOrder) {
                throw new DMCServiceException(DMCError.BadURL, "specified order, but last field already had order");
            }
            if (order.equals(SORT_ASCENDING) || order.equals(SORT_DESCENDING)) {
                orderByClause += " " + order;
            } else {
                throw new DMCServiceException(DMCError.BadURL, "invalid sort option: " + order);
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
