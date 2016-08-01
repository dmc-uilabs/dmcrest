package org.dmc.services.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import org.dmc.services.DMCServiceException;

public class SQLUtilsTest {
    private final ArrayList<String> validSortFields = new ArrayList<String>();
    
    @Before
    public void setup() {
        validSortFields.add("xxx");
        validSortFields.add("abc");
    }

    @Test
    public void setsStringToNullIfNoOrderOrSort() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, null, validSortFields);
        assertNull(orderByClause);
    }

    @Test
    public void setsStringToNullIfEmptyOrderAndSort() {
        final String orderByClause = SQLUtils.buildOrderByClause("", "", validSortFields);
        assertNull(orderByClause);
    }

    @Test
    public void setsOrderToStringWithoutSortOption() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc", validSortFields);
        assertEquals("ORDER abc", orderByClause);
    }

    @Test
    public void setsOrderToTrimmedStringWithoutSortOption() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, " abc ", validSortFields);
        assertEquals("ORDER abc", orderByClause);
    }

    @Test
    public void setsStringToNullIfSortIsNull() {
        final String orderByClause = SQLUtils.buildOrderByClause("couldBeAnything", null, validSortFields);
        assertNull(orderByClause);
    }

    @Test
    public void setsOrderWithAscending() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc", validSortFields);
        assertEquals("ORDER abc ASC", orderByClause);
    }

    @Test
    public void setsOrderWithDescending() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_DESCENDING, " xxx ", validSortFields);
        assertEquals("ORDER xxx DESC", orderByClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionIfNotAscendingOrDescendingOrder() {
        SQLUtils.buildOrderByClause("invalidOrder", " xxx ", validSortFields);
    }

    @Test
    public void setsOrderForMultipleFields() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc, xxx", validSortFields);
        assertEquals("ORDER abc, xxx", orderByClause);
    }

    @Test
    public void setsOrderForMultipleFieldsWithOrder() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc, xxx", validSortFields);
        assertEquals("ORDER abc, xxx ASC", orderByClause);
    }

    @Test
    public void setsOrderForMultipleFieldsWithOrderInSortFields() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc   DESC,   xxx ASC", validSortFields);
        assertEquals("ORDER abc DESC, xxx ASC", orderByClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionIfOrderSetAndInFieldSpec() {
        SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc   DESC,   xxx ASC", validSortFields);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionForBadFieldWithMultipleFields() {
        SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "def, xxx", validSortFields);
    }

    @Test
    public void setsStringToNullIfLimitIsNull() {
        final String limitClause = SQLUtils.buildLimitClause(null);
        assertNull(limitClause);
    }

    @Test
    public void setsLimitString() {
        final String limitClause = SQLUtils.buildLimitClause(15);
        assertEquals("LIMIT 15", limitClause);
    }

}
