package org.dmc.services.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class AWSLinkValidatorNew implements ConstraintValidator<AWSLink, String>{

	@Override
	public void initialize(AWSLink constraintAnnotation) {
		
	}



	@Override
	public boolean isValid(String url, ConstraintValidatorContext context) {
		if (url == null || url.isEmpty()) return false;
		
		if(!url.startsWith("https://") 
				|| !(url.indexOf(System.getenv("AWS_UPLOAD_BUCKET")) != -1
				|| url.indexOf(System.getenv("AWS_UPLOAD_BUCKET_FINAL")) != -1)) {
			return false;
		}
		else {
			return true;
		}
	}
}
