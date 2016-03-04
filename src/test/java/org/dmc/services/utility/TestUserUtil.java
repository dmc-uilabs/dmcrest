package org.dmc.services.utility;

import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

public class TestUserUtil {
    public static String generateTime() {
        Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String unique = format.format(date);
        return unique;
    }

    public static String uniqueID() {
        UUID uniqueID = UUID.randomUUID();
        return uniqueID.toString();
    }
    
    public static String uniqueUserEPPN() {
        UUID uniqueID = UUID.randomUUID();
        return "uniqueUser" + uniqueID.toString();
    }
}