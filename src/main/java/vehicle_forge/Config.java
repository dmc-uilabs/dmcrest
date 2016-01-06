package vehicle_forge;

public class Config {

	public static final String DB_PORT = System.getenv("DBport");

	public static final String DB_HOST = "jdbc:postgresql://" + System.getenv("DBip");
 		
        public static final String DB_NAME = "gforge";
 	public static final String DB_USER = System.getenv("DBuser");
 	public static final String DB_PASS = System.getenv("DBpass");

	//LOGGING
 	public static final String LOG_FILE = "logs/dmc_site_services.log";
    public static final boolean CONSOLE_LOGGING = true;
}
