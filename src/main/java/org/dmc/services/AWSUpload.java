package org.dmc.services;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.dmc.services.ServiceLogger;
import org.dmc.services.services.ServiceController;
import org.dmc.services.DMCServiceException;


/*
 * A Helper class for file uploads and signed URL retrieval from AWS S3 
 * 
 */
public class AWSUpload {
	private final String logTag = AWSUpload.class.getName();

	//The Temp Bucket where resource is initially stored by frontend Upload
	private static String sourceBucket   = "dmc-uploads2";
	private static String sourceKey      = "dmdii-logo.png";
	
	//The Perm Bucket where resource
	private static String destBucket     = "dmc-profiletest";
	private static String destKey        = "dmdii-logo-sanitized.png";

	private static String accessKey = "AKIAJHHCWWBAHZPREBCQ";
	private static String secretKey = "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";

	
	
	//Function should take in sourceKey and destinationKey as parameters
	public String Upload(String source, String destination, String userEPPN) throws DMCServiceException { 
		
		ServiceLogger.log(logTag, "User" + userEPPN + "uploading object from " + sourceBucket + " to S3 bucket " + destBucket);

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = new AmazonS3Client(awsCreds);
        String preSignedURL = null; 

        try {
        	
            // Copying object, AWS takes care of all implementation of this. 
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(
            		sourceBucket, sourceKey, destBucket, destKey);
            System.out.println("Copying object.");
            s3client.copyObject(copyObjRequest);

        } catch (AmazonServiceException ase) {
        	
        	//For testing 
            System.out.println("Caught an AmazonServiceException, " +
            		"which means your request made it " + 
            		"to Amazon S3, but was rejected with an error " +
                    "response for some reason.");
            
            
            //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message:    " + ase.getMessage());
            ServiceLogger.log(logTag,"HTTP Status Code: " + ase.getStatusCode());
            ServiceLogger.log(logTag,"AWS Error Code:   " + ase.getErrorCode());
            ServiceLogger.log(logTag,"Error Type:       " + ase.getErrorType());
            ServiceLogger.log(logTag,"Request ID:       " + ase.getRequestId());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());

        } catch (AmazonClientException ace) {
        	
        	//For testing 
            System.out.println("Caught an AmazonClientException, " +
            		"which means the client encountered " +
                    "an internal error while trying to " +
                    " communicate with S3, " +
                    "such as not being able to access the network.");
            
            //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " encountered internal error communication with "
					+ "S3 and rejected do to: " + ace.getMessage());
        }
        
		try {
			
			//For testing
			System.out.println("Generating pre-signed URL.");
			
			//Parameters for Request
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);
	
			GeneratePresignedUrlRequest generatePresignedUrlRequest = 
				    new GeneratePresignedUrlRequest(destBucket, destKey);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
			generatePresignedUrlRequest.setExpiration(expiration);
	
			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest); 
			preSignedURL = url.toString();
			
			//For testing
			System.out.println("Pre-Signed URL = " + preSignedURL);
						
		} catch (AmazonServiceException ase) {
			
			//For testing
			System.out.println("Caught an AmazonServiceException, " +
					"which means your request made it " +
					"to Amazon S3, but was rejected with an error response " +
			"for some reason.");
			   //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message:    " + ase.getMessage());
            ServiceLogger.log(logTag,"HTTP Status Code: " + ase.getStatusCode());
            ServiceLogger.log(logTag,"AWS Error Code:   " + ase.getErrorCode());
            ServiceLogger.log(logTag,"Error Type:       " + ase.getErrorType());
            ServiceLogger.log(logTag,"Request ID:       " + ase.getRequestId());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());

            
		} catch (AmazonClientException ace) {
			
			//For testing
			System.out.println("Caught an AmazonClientException, " +
					"which means the client encountered " +
					"an internal error while trying to communicate" +
					" with S3, " +
			"such as not being able to access the network.");			
			
	        //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " encountered internal error communication with "
					+ "S3 and rejected do to: " + ace.getMessage());
		}

			//If fails
			return preSignedURL; 
	}
}
