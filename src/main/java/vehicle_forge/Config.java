package vehicle_forge;

public class Config {

	// DB
	public static final String DB_PORT = System.getenv("DBport");
	public static final String DB_HOST = "jdbc:postgresql://" + System.getenv("DBip");
    public static final String DB_NAME = "gforge";
 	public static final String DB_USER = System.getenv("DBuser");
 	public static final String DB_PASS = System.getenv("DBpass");
 	
    // Documentation
    public static final String DOC_API_NAME = "DMC Rest Services";
    public static final String DOC_API_DESCRIPTION = "Serves the DMC Frontend Application";
    public static final String DOC_API_VERSION = "0.1.0";
    public static final String DOC_API_URL = "http://terms-of-service.url";
    public static final String DOC_API_CONTACT = "Contact Name <contact@contact.com>";
    public static final String DOC_API_LICENSE = "License";
    public static final String DOC_API_LICENSE_URL = "http://licenseurl";

	//Loging
 	public static final String LOG_FILE = "logs/dmc_site_services.log";
    public static final boolean CONSOLE_LOGGING = true;
}
