package org.dmc.services;

public class Config {

	// DB
	public static final String DB_PORT = "5432"; //System.getenv("DBport");//"5432";//System.getenv("DBport");
	public static final String DB_HOST = "jdbc:postgresql://" + "52.36.15.141"; //System.getenv("DBip");//ec2-52-35-193-110.us-west-2.compute.amazonaws.com";// + System.getenv("DBip");
    public static final String DB_NAME = "gforge";//"gforge";
 	public static final String DB_USER = "gforge";//System.getenv("DBuser");//"gforge";//System.getenv("DBuser");
 	public static final String DB_PASS = "gforge"; //System.getenv("DBpass");//"gforge";//System.getenv("DBpass");
 	
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
