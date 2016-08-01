package org.dmc.services.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.dmc.services.DMCServiceException;

public class SQLUtilsTest {

    @Test
    public void setsStringToNullIfNoOrderOrSort() {
        String orderByClause = SQLUtils.buildOrderByClause(null, null);
        assertNull(orderByClause);
    }

    @Test
    public void setsStringToNullIfEmptyOrderAndSort() {
        String orderByClause = SQLUtils.buildOrderByClause("", "");
        assertNull(orderByClause);
    }

    @Test
    public void setsOrderToStringWithoutSortOption() {
        String orderByClause = SQLUtils.buildOrderByClause(null, "abc");
        assertEquals("ORDER abc", orderByClause);
    }

    @Test
    public void setsOrderToTrimmedStringWithoutSortOption() {
        String orderByClause = SQLUtils.buildOrderByClause(null, " abc ");
        assertEquals("ORDER abc", orderByClause);
    }

    @Test
    public void setsStringToNullIfSortIsNull() {
        String orderByClause = SQLUtils.buildOrderByClause("couldBeAnything", null);
        assertNull(orderByClause);
    }

    @Test
    public void setsOrderWithAscending() {
        String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc");
        assertEquals("ORDER abc ASC", orderByClause);
    }

    @Test
    public void setsOrderWithDescending() {
        String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_DESCENDING, " xxx ");
        assertEquals("ORDER xxx DESC", orderByClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionIfNotAscendingOrDescendingOrder() {
        SQLUtils.buildOrderByClause("invalidOrder", " xxx ");
    }

    @Test
    public void setsStringToNullIfLimitIsNull() {
        String limitClause = SQLUtils.buildLimitClause(null);
        assertNull(limitClause);
    }

    @Test
    public void setsLimitString() {
        String limitClause = SQLUtils.buildLimitClause(15);
        assertEquals("LIMIT 15", limitClause);
    }

}
