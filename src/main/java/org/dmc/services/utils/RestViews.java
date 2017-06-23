package org.dmc.services.utils;

public class RestViews {
	
  /**
 * @author emiliano
 * View for the "short" list of info for companies
 * Only returns "id", "accountId", and "name"
 */
public static class CompaniesShortView {}

/**
 * @author emiliano
 * Returns DOME Server information without IP address
 */
public static class SecureServerView {}

/**
 * @author emiliano
 * Returns Document entity without S3 URL
 */
public static class SDocumentsView {}

}


