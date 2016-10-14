package org.dmc.services.verification;

import org.dmc.services.DMCServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class VerificationPatchController {

	@Inject
	private VerificationPatchDao verificationPatchDao;

	@RequestMapping(value = "/verify", method = RequestMethod.POST, consumes = "application/json", produces = {"application/json"})
	public ResponseEntity verify(@RequestBody VerificationPatch payload) {
		try {
			VerificationPatch patchedURL = verificationPatchDao.verify(payload);
			return new ResponseEntity(patchedURL, HttpStatus.valueOf(HttpStatus.OK.value()));

		} catch (DMCServiceException e) {
			return new ResponseEntity(e.getMessage(), e.getHttpStatusCode());
		}
	}
}
