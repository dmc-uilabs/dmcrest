package vehicle_forge;

public class Config {

	public static final String DB_PORT = "5432";

	public static final String DB_HOST = "jdbc:postgresql://ec2-54-173-180-180.compute-1.amazonaws.com";
 		
        public static final String DB_NAME = "gforge";
 	public static final String DB_USER = "gforge";
 	public static final String DB_PASS = "gforge";

	//LOGGING
 	public static final String LOG_FILE = "logs/dmc_site_services.log";
    public static final boolean CONSOLE_LOGGING = true;
}
