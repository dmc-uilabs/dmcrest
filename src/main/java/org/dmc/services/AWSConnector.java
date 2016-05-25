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
import com.amazonaws.services.s3.model.DeleteObjectRequest;


import org.dmc.services.ServiceLogger;
import org.dmc.services.services.ServiceController;
import org.dmc.services.DMCServiceException;

/*
 * A Helper class for file uploads and signed URL retrieval from AWS S3
 *
 */
public class AWSConnector {
	private final String logTag = AWSConnector.class.getName();

	//The Temp Bucket where resource is initially stored by FrontEnd Upload
	//SOURCE AND DEST BUCKETS SHOULD BE ENV VARIABLES!
	private static String sourceBucket = $BAMBOO_S3SOURCEBUCKET;//System.getenv("S3SourceBucket");
	//private static String sourceKey = "test/cat.jpeg";

	//The Perm Bucket where resource
	private static String destBucket = $BAMBOO_S3DESTBUCKET; //${bamboo.S3SourceBucket} //System.getenv("S3DestBucket");

	private static String accessKey = $BAMBOO_S3ACCESSKEY;//${bamboo.S3SourceBucket} //System.getenv("S3AccessKey");
	private static String secretKey = $BAMBOO_S3SECRETKEY;//${bamboo.S3SourceBucket} //System.getenv("S3SecretKey");


	//Source is the path the the resource in the bucket
	public String Upload(String tempURL, String userEPPN) throws DMCServiceException {
		String destPath = "testing";
		ServiceLogger.log(logTag, "User" + userEPPN + "uploading object from " + sourceBucket + " to S3 bucket " + destBucket);

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = new AmazonS3Client(awsCreds);
        String preSignedURL = null;

        //Convert temp URL to sourceKey
		String sourceKey = tempURL.substring(tempURL.indexOf("com/") + 4, tempURL.length());

		//Remove bucket name
		sourceKey = sourceKey.substring(sourceKey.indexOf('/') + 1 , sourceKey.length());

        //Convert temp URL to filename
        String filename = tempURL.substring(tempURL.lastIndexOf('/') + 1, tempURL.length());


        //Using a General Hash Code Function for now. Future hash functions should generated unique hashes
        String hashCode = Integer.toString(filename.hashCode()%10000);


        //Throws error if file invalid
        if(filename == null || filename.length() == 0){
        	throw new DMCServiceException(DMCError.Generic, "File from" + userEPPN + "is invalid ");
        }

        //Throws error if source path invalid
        if (destPath == null || destPath.length() == 0){
        	//NEED to add DMC method for AWS exceptions
        	throw new DMCServiceException(DMCError.Generic, "Source path from" + userEPPN + "is invalid ");
        }

        int findDot = filename.lastIndexOf(".");

        if (findDot == -1){
        	//NEED to add DMC method for AWS exception
        	throw new DMCServiceException(DMCError.Generic, "User " + userEPPN + "gave Invalid file extension name");
        }

        //Rename and Parse
        String parsedSource = filename.substring(0, findDot) + hashCode + "-sanitized" + filename.substring(findDot, filename.length());


        //String Concatenation for location // + destPath + "/" +
        String destKey = "profiles/" + userEPPN + "/"  + parsedSource;


        try {

            // Copying object, AWS takes care of all implementation of this.
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucket, sourceKey, destBucket, destKey);
            System.out.println("Copying object.");
            s3client.copyObject(copyObjRequest);

        } catch (AmazonServiceException ase) {

        	ServiceLogger.log(logTag,"Caught an AmazonServiceException, " + "which means your request made it "
        	+ "to Amazon S3, but was rejected with an error " + "response for some reason.");

            //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message:    " + ase.getMessage());
            ServiceLogger.log(logTag,"HTTP Status Code: " + ase.getStatusCode());
            ServiceLogger.log(logTag,"AWS Error Code:   " + ase.getErrorCode());
            ServiceLogger.log(logTag,"Error Type:       " + ase.getErrorType());
            ServiceLogger.log(logTag,"Request ID:       " + ase.getRequestId());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " made it, rejected do to: " + ase.getMessage());

        } catch (AmazonClientException ace) {

            ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
            + " while trying to communicate with S3, such as not being able to access the network.");

            //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " encountered internal error with "
			+ "S3 and rejected do to: " + ace.getMessage());
        } try {

        	ServiceLogger.log(logTag,"Generating pre-signed URL.");

			//Parameters for Request, can change expiration time to any amount.
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(destBucket, destKey);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			preSignedURL = url.toString();

		} catch (AmazonServiceException ase) {

			//For testing
			ServiceLogger.log(logTag,"Caught an AmazonServiceException which means your request made it "
			+ "to Amazon S3, but was rejected with an error response for some reason.");

			//Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message:    " + ase.getMessage());
            ServiceLogger.log(logTag,"HTTP Status Code: " + ase.getStatusCode());
            ServiceLogger.log(logTag,"AWS Error Code:   " + ase.getErrorCode());
            ServiceLogger.log(logTag,"Error Type:       " + ase.getErrorType());
            ServiceLogger.log(logTag,"Request ID:       " + ase.getRequestId());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());


		} catch (AmazonClientException ace) {

		    ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
		    + " while trying to communicate with S3, such as not being able to access the network.");

	        //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
			throw new DMCServiceException(DMCError.Generic, "AWS Upload Request from " + userEPPN + " encountered internal error communication with "
			+ "S3 and rejected do to: " + ace.getMessage());
		}
		return preSignedURL;
	}

	public String Remove (String URL, String userEPPN) throws DMCServiceException {

		//Parse URL to get Path
		int ResourcePathStart = URL.indexOf("com/") + 4;
		int ResourcePathEnd = URL.indexOf("?A");
		String ResourcePath = URL.substring(ResourcePathStart, ResourcePathEnd);
		ServiceLogger.log(logTag, "User: " + userEPPN + " deleting " + ResourcePath + " from S3 bucket: " + destBucket);

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = new AmazonS3Client(awsCreds);

        try {
            // Deleting object, AWS takes care of all implementation of this.
            s3client.deleteObject(new DeleteObjectRequest(destBucket, ResourcePath));

        } catch (AmazonServiceException ase) {

        	ServiceLogger.log(logTag,"Caught an AmazonServiceException, " + "which means your request made it "
        	+ "to Amazon S3, but was rejected with an error " + "response for some reason.");


			//Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message:    " + ase.getMessage());
            ServiceLogger.log(logTag,"HTTP Status Code: " + ase.getStatusCode());
            ServiceLogger.log(logTag,"AWS Error Code:   " + ase.getErrorCode());
            ServiceLogger.log(logTag,"Error Type:       " + ase.getErrorType());
            ServiceLogger.log(logTag,"Request ID:       " + ase.getRequestId());

			throw new DMCServiceException(DMCError.Generic, "AWS Delete Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());
        } catch (AmazonClientException ace) {
        	ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
            + " while trying to communicate with S3, such as not being able to access the network.");

	        //Detailed Error Logging
            ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
			throw new DMCServiceException(DMCError.Generic, "AWS Delete Request from " + userEPPN + " encountered internal error communication with "
		    + "S3 and rejected do to: " + ace.getMessage());
        }

        return ResourcePath;

	}//Remove


}//End class
