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
    public void returnsEmptyStringIfNullOrderOrSort() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, null, validSortFields);
        assertEquals("", orderByClause);
    }

    @Test
    public void returnsEmptyStringIfEmptyOrderAndSort() {
        final String orderByClause = SQLUtils.buildOrderByClause("", "", validSortFields);
        assertEquals("", orderByClause);
    }

    @Test
    public void setsOrderToStringWithoutSortOption() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc", validSortFields);
        assertEquals("ORDER BY abc ", orderByClause);
    }

    @Test
    public void setsOrderToTrimmedStringWithoutSortOption() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, " abc ", validSortFields);
        assertEquals("ORDER BY abc ", orderByClause);
    }

    @Test
    public void returnsEmptyStringIfSortIsNull() {
        final String orderByClause = SQLUtils.buildOrderByClause("couldBeAnything", null, validSortFields);
        assertEquals("", orderByClause);
    }

    @Test
    public void setsOrderWithAscending() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc", validSortFields);
        assertEquals("ORDER BY abc ASC ", orderByClause);
    }

    @Test
    public void setsOrderWithDescending() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_DESCENDING, " xxx ", validSortFields);
        assertEquals("ORDER BY xxx DESC ", orderByClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionIfNotAscendingOrDescendingOrder() {
        SQLUtils.buildOrderByClause("invalidOrder", " xxx ", validSortFields);
    }

    @Test
    public void setsOrderForMultipleFields() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc, xxx", validSortFields);
        assertEquals("ORDER BY abc , xxx ", orderByClause);
    }

    @Test
    public void setsOrderForMultipleFieldsWithOrder() {
        final String orderByClause = SQLUtils.buildOrderByClause(SQLUtils.SORT_ASCENDING, "abc, xxx", validSortFields);
        assertEquals("ORDER BY abc , xxx ASC ", orderByClause);
    }

    @Test
    public void setsOrderForMultipleFieldsWithOrderInSortFields() {
        final String orderByClause = SQLUtils.buildOrderByClause(null, "abc   DESC,   xxx ASC", validSortFields);
        assertEquals("ORDER BY abc DESC , xxx ASC ", orderByClause);
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
    public void returnsEmptyStringIfLimitIsNull() {
        final String limitClause = SQLUtils.buildLimitClause(null);
        assertEquals("", limitClause);
    }

    @Test
    public void setsLimitString() {
        final String limitClause = SQLUtils.buildLimitClause(15);
        assertEquals("LIMIT 15 ", limitClause);
    }

    @Test
    public void setsOffsetString() {
        final String offsetClause = SQLUtils.buildOffsetClause(5);
        assertEquals("OFFSET 5 ", offsetClause);
    }

    @Test
    public void returnsEmptyStringIfStartIsNull() {
        final String offsetClause = SQLUtils.buildOffsetClause(null);
        assertEquals("", offsetClause);
    }

    @Test
    public void returnsCorrectResultForDay() {
        final String dateClause = SQLUtils.buildDateClause("6d", "testField", false);
        assertEquals("WHERE testField >= CURRENT_DATE - INTERVAL '6 day' ", dateClause);
    }

    @Test
    public void returnsCorrectResultForMonth() {
        final String dateClause = SQLUtils.buildDateClause("4m", "testDateField", false);
        assertEquals("WHERE testDateField >= CURRENT_DATE - INTERVAL '4 month' ", dateClause);
    }

    @Test
    public void returnsCorrectResultForYear() {
        final String dateClause = SQLUtils.buildDateClause("3y", "testField", false);
        assertEquals("WHERE testField >= CURRENT_DATE - INTERVAL '3 year' ", dateClause);
    }

    @Test
    public void returnsCorrectResultForAppending() {
        final String dateClause = SQLUtils.buildDateClause("4m", "testDateField", true);
        assertEquals("AND testDateField >= CURRENT_DATE - INTERVAL '4 month' ", dateClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionWhenDateCriteriaHasBadType() {
        SQLUtils.buildDateClause("7x", "testDateField", false);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionWhenDateCriteriaHasBadNumber() {
        SQLUtils.buildDateClause("w3y", "testDateField", false);
    }

    @Test
    public void returnsEmptyStringIfDateCriteriaIsNull() {
        final String dateClause = SQLUtils.buildDateClause(null, "testDateField", false);
        assertEquals("", dateClause);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionWhenFieldNameIsNull() {
        SQLUtils.buildDateClause("4m", null, false);
    }

    @Test(expected = DMCServiceException.class)
    public void throwsExceptionWhenFieldNameIsEmpty() {
        SQLUtils.buildDateClause("4m", "", false);
    }

}
