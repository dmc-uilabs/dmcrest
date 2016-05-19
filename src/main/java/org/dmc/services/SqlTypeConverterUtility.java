package org.dmc.services;

import java.sql.Date;

/**
 * This class will convert Strings to desired type if value is not null,
 * otherwise will return null.
 * This can then be used to extract info from an object to put in a PreparedStatement
 * @author 200001069
 *
 */
public class SqlTypeConverterUtility {
    public static Integer getInt(String value)
    {
        if (null != value) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }
    
    public static java.sql.Date getSqlDate(java.util.Date value)
    {
        if (null != value) {
            return new java.sql.Date(value.getTime());
        } else {
            return null;
        }
    }
}
