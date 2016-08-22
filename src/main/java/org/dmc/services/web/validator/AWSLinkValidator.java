package org.dmc.services.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amazonaws.services.config.model.ValidationException;

@Component
public class AWSLinkValidator implements Validator{
	
	private static final String AWS_LINK_VALIDATION_ERROR_MESSAGE = "Malformed link detected";
	private static final String AWS_LINK_MISSING_ERROR_MESSAGE = "Link is required";

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	/**
	 * Validates that the given string is a link pointing to the final AWS storage
	 * 
	 * @param target the object to be validated
	 * @param errors the contextual state of the validation process
	 * @throws ValidationException
	 */
	@Override
	public void validate(Object target, Errors errors) {
		if (target instanceof String) {
			if(!((String) target).startsWith("https://") 
					|| !(((String) target).indexOf(System.getenv("AWS_UPLOAD_BUCKET")) != -1
					|| ((String) target).indexOf(System.getenv("AWS_UPLOAD_BUCKET_FINAL")) != -1))
				throw new ValidationException(AWS_LINK_VALIDATION_ERROR_MESSAGE);
			} else if (target == null) {
			throw new ValidationException(AWS_LINK_MISSING_ERROR_MESSAGE);
		 }
	}
}
