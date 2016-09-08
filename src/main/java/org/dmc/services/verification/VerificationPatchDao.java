package org.dmc.services.verification;

import org.dmc.services.AWSConnector;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

public class VerificationPatchDao {

	private static final String logTag = VerificationPatchDao.class.getName();

	public VerificationPatch verify(VerificationPatch payload) throws DMCServiceException {
		ServiceLogger.log(logTag, "Verification Machine DAO");

		String finalURL = " ";

		//Tests to see if verification function returns true
		if (payload.isVerified()) {
			AWSConnector AWS = new AWSConnector();
			//Copy and paste to secure bucket
			finalURL = AWS
					.upload(payload.getUrl(), payload.getFolder(), payload.getUserEPPN(), payload.getResourceType());
		}
		payload.setUrl(finalURL);

		// update correct table entity return finalURL;
		String query =
				"UPDATE " + payload.getTable() + " SET " + payload.getUrlColumn() + " = ?, verified = ? " + "WHERE "
						+ payload.getIdColumn() + " = ?";

		String statement = String.format("UPDATE %s SET %s = '%s', verified = %s WHERE %s = %s", payload.getTable(),
				payload.getUrlColumn(), finalURL, payload.isVerified(), payload.getIdColumn(), payload.getId());

		DBConnector.jdbcTemplate().update(statement);

		return payload;
	}
}
