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

        String orderByClause = "";
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

        if (orderByClause.length() > 0) {
            orderByClause = orderByClause.substring(0, orderByClause.length()-2);   // remove last ", "
        }
        // order only added if ORDER BY
        if (orderByClause.length() > 0 && order != null && order.trim().length() >0) {
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

        String limitClause = "";
        if (limit != null) {
            limitClause = "LIMIT " + limit;
        }
        return limitClause;
    }

    /**
     * Build a OFFSET clause
     * OFFSET start
     * This starts the result set at the "start" record number instead of the first.
     * The start value is 0-based
     * @param start
     * @return null or the OFFSET clause
     */
    public static String buildOffsetClause(Integer start) {
        String offsetClause = "";
        if (start != null) {
            offsetClause = "OFFSET " + start;
        }
        return offsetClause;
    }
    /**
     * Build a dates query clause
     * fieldName >= current_date - interval 'n intervalType'
     * example: "WHERE release_date > current_date - interval '1 month' "
     * dateCriteria is a number followed by d (for day), m for month, y for year
     * @dateCriteria string like 7d, 1m, 1y that indicates how far back to search
     * @fieldName string that is the date/timestamp field name in the database table being queried
     * @appending boolean, if true then connect with AND, otherwise with WHERE
     * @return null or the date condition clause
     */
    public static String buildDateClause (String dateCriteria, String fieldName, boolean appending)  {
        String dateClause = "";
        String intervalType = null;
        if (null != dateCriteria && dateCriteria.length() > 0) {
            if (dateCriteria.toLowerCase().endsWith("d")) {
                intervalType = "day";
            } else if (dateCriteria.toLowerCase().endsWith("m")) {
                intervalType = "month";
            } else if (dateCriteria.toLowerCase().endsWith("y")) {
                intervalType = "year";
            } else {
                throw new DMCServiceException(DMCError.BadURL, "invalid date criteria - type");
            }
            if (null == fieldName || fieldName.length() == 0) {
                throw new DMCServiceException(DMCError.OtherSQLError, "no date fieldname specified for query");
            }
            final String intervalCount = dateCriteria.substring(0, dateCriteria.length()-1);
            try {
                Integer.parseUnsignedInt(intervalCount);
            } catch (Exception e) { 
                throw new DMCServiceException(DMCError.OtherSQLError, "invalid date criteria - number");
            }
            final String appendClause = appending ? "AND " : "WHERE ";
            dateClause = appendClause + fieldName + " >= CURRENT_DATE - INTERVAL '" + intervalCount + " " + intervalType + "' ";
        }
        return dateClause;
    }

    public static boolean isListValidIntegers(String list) {
        String[] ids = list.split(",");
        for (String id : ids) {
            try {
                Integer.parseInt(id);       // checking that we don't throw NumberFormatException, if ids become GUIDs, would need a different check
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIdInList(String id, String list) {
        if (null != list) {
            final String[] items = list.split(",");
            for (String item : items) {
                if (item.equals(id)) return true;
            }
        }
        return false;
    }

    public static String formatClauses(List<String> clauses, boolean startWithWhere) {
        String queryClause = "";
        final String firstClause = startWithWhere ? "WHERE " : "AND ";
        if (clauses.size() > 0) {
            queryClause += firstClause + clauses.get(0);
            for (int i = 1; i < clauses.size(); ++i) {
                queryClause += " AND " + clauses.get(i);
            }
        }
        return queryClause;
    }
}
