package org.dmc.services;

public class Config {

	// DB
	public static final String DB_PORT = "5432";
	public static final String DB_HOST = "jdbc:postgresql://54.172.82.133";
    public static final String DB_NAME = "gforge";
 	public static final String DB_USER = "gforge";
 	public static final String DB_PASS = "gforge";

 	public static final String IS_TEST = System.getenv("isTest");

    // Documentation
    public static final String DOC_API_NAME = "DMC Rest Services";
    public static final String DOC_API_DESCRIPTION = "Serves the DMC Frontend Application";
    public static final String DOC_API_VERSION = "0.1.0";
    public static final String DOC_API_URL = "http://terms-of-service.url";
    public static final String DOC_API_CONTACT = "DMC <dmc@uilabs.org>";
    public static final String DOC_API_LICENSE = "License";
    public static final String DOC_API_LICENSE_URL = "http://licenseurl";

	//Loging
 	public static final String LOG_FILE = "logs/dmc_site_services.log";
    public static final boolean CONSOLE_LOGGING = true;
}
