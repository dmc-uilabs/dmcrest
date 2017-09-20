package org.dmc.services.utils;

public class RestViews {
	
public static class Public {}
	
  /**
 * @author emiliano
 * View for the "short" list of info for companies
 * Only returns "id", "accountId", and "name"
 */
public static class CompaniesShortView extends Public {}

/**
 * @author emiliano
 * Returns DOME Server information without IP address
 */
public static class SecureServerView extends Public {}

/**
 * @author emiliano
 * Returns Document entity without S3 URL
 */
public static class SDocumentsView extends Public {}

public static class SimpleUserView extends Public {}

}




