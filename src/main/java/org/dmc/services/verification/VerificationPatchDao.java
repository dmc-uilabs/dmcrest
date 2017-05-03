package org.dmc.services.verification;

import org.dmc.services.AWSConnector;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class VerificationPatchDao {

	private static final Logger logger = LoggerFactory.getLogger(VerificationPatchDao.class);

	@Inject
	private DocumentService documentService;

	public VerificationPatch verify(VerificationPatch payload) throws DMCServiceException {
		logger.info("Request to verify payload: {}", payload);

		String finalURL = "kadjasd";
		// String finalURL = " ";

		// if (payload.isVerified()) {
		// 	AWSConnector AWS = new AWSConnector();
		// 	finalURL = AWS
		// 			.upload(payload.getUrl(), payload.getFolder(), payload.getUserEPPN(), payload.getResourceType());
		// }

		payload.setUrl(finalURL);

		if ("document".equals(payload.getTable())) {
			this.documentService.updateVerifiedDocument(payload.getId(), payload.getUrl(), payload.isVerified(), payload.getSha256());
		} else {
			// update correct table entity return finalURL;


			String query =
					"UPDATE " + payload.getTable() + " SET " + payload.getUrlColumn() + " = ?, verified = ?, sha256 = ? " + "WHERE "
							+ payload.getIdColumn() + " = ?";


			String statement = String.format("UPDATE %s SET %s = '%s', verified = %s, sha256='%s' WHERE %s = %s", payload.getTable(),
					payload.getUrlColumn(), finalURL, payload.isVerified(), payload.getSha256(), payload.getIdColumn(), payload.getId());


			DBConnector.jdbcTemplate().update(statement);
		}

		return payload;
	}
}
