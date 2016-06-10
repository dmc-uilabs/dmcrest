package org.dmc.services;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCServiceException;

/*
 * A Helper class for file uploads and signed URL retrieval from AWS S3
 *
 */
public class AWSConnector {
	private final String logTag = AWSConnector.class.getName();

	//The Temp Bucket where resource is initially stored by FrontEnd Upload
	//SOURCE AND DEST BUCKETS SHOULD BE ENV VARIABLES!
	private static String sourceBucket = System.getenv("S3SourceBucket");
	//private static String sourceKey = "test/cat.jpeg";

	//The Perm Bucket where resource
	private static String destBucket = System.getenv("S3DestBucket");

	private static String accessKey = System.getenv("S3AccessKey");
	private static String secretKey = System.getenv("S3SecretKey");

	//Source is the path the the resource in the bucket
	public String upload(String tempURL, String Folder, String userEPPN, String ResourceType) throws DMCServiceException {

	    if (null == tempURL) {
	        ServiceLogger.log(logTag, "User" + userEPPN + " - no item to upload, returning");
	        return null;
	    }
	   ServiceLogger.log(logTag, "User" + userEPPN + "uploading object from " + sourceBucket + " to S3 bucket " + destBucket);

	   BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
       AmazonS3 s3client = new AmazonS3Client(awsCreds);
       String preSignedURL = null;

       //Create the destPath
       String destPath = Folder + "/" + userEPPN + "/" + ResourceType;

       //Throws error if source path invalid
       if (destPath == null || destPath.length() == 0){
    	   //NEED to add DMC method for AWS exceptions
    	   throw new DMCServiceException(DMCError.AWSError, "Source path from" + userEPPN + "is invalid ");
       }

       //Convert temp URL to sourceKey
	   String sourceKey = tempURL.substring(tempURL.indexOf("com/") + 4, tempURL.length());

	   //Remove bucket name
	   sourceKey = sourceKey.substring(sourceKey.indexOf('/') + 1 , sourceKey.length());

       String filename = tempURL.substring(tempURL.lastIndexOf('/') + 1, tempURL.length());

       //Throws error if file invalid
       if(!isFileNameValid(filename)){
    	 throw new DMCServiceException(DMCError.AWSError, "File from" + userEPPN + "is invalid ");
       }

       String destKey = createDestKey(destPath,filename);

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
		 throw new DMCServiceException(DMCError.AWSError, "AWS Upload Request from " + userEPPN + " made it, rejected do to: " + ase.getMessage());

       } catch (AmazonClientException ace) {

         ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
         + " while trying to communicate with S3, such as not being able to access the network.");

         //Detailed Error Logging
         ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
	     throw new DMCServiceException(DMCError.AWSError, "AWS Upload Request from " + userEPPN + " encountered internal error with "
	     + "S3 and rejected do to: " + ace.getMessage());
      }
	   try {

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
			throw new DMCServiceException(DMCError.AWSError, "AWS Upload Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());

	   } catch (AmazonClientException ace) {

         ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
         + " while trying to communicate with S3, such as not being able to access the network.");

        //Detailed Error Logging
        ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
	    throw new DMCServiceException(DMCError.AWSError, "AWS Upload Request from " + userEPPN + " encountered internal error communication with "
	    + "S3 and rejected do to: " + ace.getMessage());
	  }
		return preSignedURL;
	}

	public String remove (String URL, String userEPPN) throws DMCServiceException {

	    if (null == URL) {
	        ServiceLogger.log(logTag, "User: " + userEPPN + " path to resource is null, returning");
	        return null;
	    }
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
		throw new DMCServiceException(DMCError.AWSError, "AWS Delete Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());
      } catch (AmazonClientException ace) {
    	ServiceLogger.log(logTag,"Caught an AmazonClientException, which means the client encountered an internal error"
        + " while trying to communicate with S3, such as not being able to access the network.");

        //Detailed Error Logging
        ServiceLogger.log(logTag,"Error Message: " + ace.getMessage());
		throw new DMCServiceException(DMCError.AWSError, "AWS Delete Request from " + userEPPN + " encountered internal error communication with "
    	+ "S3 and rejected do to: " + ace.getMessage());
     }
    return ResourcePath;

	}//Remove

	//Function to create a path to resource in S3 to store in DB for easy future references
	public String createPath (String URL) throws DMCServiceException {
	  //Parse URL to get Path
	  try {
	    int ResourcePathStart = URL.indexOf("com/") + 4;
	    int ResourcePathEnd = URL.indexOf("?A");
	    String ResourcePath = URL.substring(ResourcePathStart, ResourcePathEnd);
        return ResourcePath;
	   } catch(Exception e){
		throw new DMCServiceException(DMCError.AWSError, "AWS create path from " + URL + "encountered internal error");
	   }
	}//createPath

	public String refreshURL(String path) throws DMCServiceException{
	  BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
      AmazonS3 s3client = new AmazonS3Client(awsCreds);

  	  ServiceLogger.log(logTag,"Refreshing pre-signed URL.");

	  //Parameters for Request, can change expiration time to any amount.
      java.util.Date expiration = new java.util.Date();
      long milliSeconds = expiration.getTime();
	  milliSeconds += 1000 * 60 * 60; // Add 1 hour.
      expiration.setTime(milliSeconds);

      GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(destBucket, path);
	  generatePresignedUrlRequest.setMethod(HttpMethod.GET);
      generatePresignedUrlRequest.setExpiration(expiration);

      URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
	  String preSignedURL = url.toString();
	  return preSignedURL;
	}

   //Helper function to check if timestamp expired
   public boolean isTimeStampExpired(Timestamp expiration){
	  // create a java calendar instance
	  Calendar calendar = Calendar.getInstance();
	  // get a java.util.Date from the calendar instance.
	  java.util.Date now = calendar.getTime();

	  // a java current time (now) instance
	  java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

	  if(expiration.after(currentTimestamp)){
	     return true;
      }
	  //should be false, but true for now for testing
      return false;
	}


   //Helper function to check Filename conventions
   private boolean isFileNameValid(String filename){

	 //Future validation checks for security will go here
     if(filename == null || filename.length() == 0){
    	return false;
     }

     return true;
    }

   //helper function to create DestKeys
   private String createDestKey(String destPath, String filename){

    //Using a General Hash Code Function for now. Future hash functions should generated unique hashes
    String hashCode = Integer.toString(filename.hashCode()%1000000);

    //Get current time from system
    long unixTime = System.currentTimeMillis() / 1000L;

    //String Concatenation for location // + destPath + "/" +
    String destKey = destPath + "/" + unixTime + "-" + hashCode + "-sanitized-" + filename; ;

    return destKey;
   }


}//End class
