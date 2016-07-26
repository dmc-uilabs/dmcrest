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
    private static final String LOGTAG = AWSConnector.class.getName();

    // The Temp Bucket where resource is initially stored by FrontEnd Upload
    // SOURCE AND DEST BUCKETS SHOULD BE ENV VARIABLES!
    private static String sourceBucket = "test-temp-verify";//System.getenv("AWS_UPLOAD_BUCKET");
    // private static String sourceKey = "test/cat.jpeg";

    // The Perm Bucket where resource
    private static String destBucket = "test-final-verify";//System.getenv("AWS_UPLOAD_BUCKET_FINAL");

    private static String accessKey = "AKIAJDE3BJULBHCYEX4Q";//System.getenv("AWS_UPLOAD_KEY");
    private static String secretKey = "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";//System.getenv("AWS_UPLOAD_SEC");

    // Source is the path the the resource in the bucket
    public static String upload(String tempURL, String Folder, String userEPPN, String ResourceType)
            throws DMCServiceException {

        if (null == tempURL) {
            ServiceLogger.log(LOGTAG, "User" + userEPPN + " - no item to upload, returning");
            return null;
        }
        ServiceLogger.log(LOGTAG,
                "User" + userEPPN + "uploading object from " + sourceBucket + " to S3 bucket " + destBucket);

        final AmazonS3 s3client = getAmazonS3Client();

        final String sourceKey = createSourceKey(tempURL);
        final String destKey = createDestKey(tempURL, Folder, userEPPN, ResourceType);

        try {
            // Copying object, AWS takes care of all implementation of this.
            final CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucket, sourceKey, destBucket, destKey);
            ServiceLogger.log(LOGTAG, "Copying object to s3 bucket.");
            s3client.copyObject(copyObjRequest);
            ServiceLogger.log(LOGTAG, "Generating pre-signed URL.");
            final String preSignedURL = refreshURL(destKey);
            return preSignedURL;

        } catch (AmazonServiceException ase) {
            logDetailsOfAmazonServiceException("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.", ase);
            throw new DMCServiceException(DMCError.AWSError,
                    "AWS Upload Request from " + userEPPN + " made it, rejected do to: " + ase.getMessage());
        } catch (AmazonClientException ace) {
            logAmazonClientException("Caught an AmazonClientException, which means the client encountered an internal error"
                            + " while trying to communicate with S3, such as not being able to access the network.", ace);
            throw new DMCServiceException(DMCError.AWSError, "AWS Upload Request from " + userEPPN
                    + " encountered internal error with " + "S3 and rejected do to: " + ace.getMessage());
        }
    }

    public static String remove(String URL, String userEPPN) throws DMCServiceException {

        if (null == URL) {
            ServiceLogger.log(LOGTAG, "User: " + userEPPN + " path to resource is null, returning");
            return null;
        }
        // Parse URL to get Path
        final int ResourcePathStart = URL.indexOf("com/") + 4;
        final int ResourcePathEnd = URL.indexOf("?A");
        final String ResourcePath = URL.substring(ResourcePathStart, ResourcePathEnd);
        ServiceLogger.log(LOGTAG, "User: " + userEPPN + " deleting " + ResourcePath + " from S3 bucket: " + destBucket);

        final AmazonS3 s3client = getAmazonS3Client();

        try {
            // Deleting object, AWS takes care of all implementation of this.
            s3client.deleteObject(new DeleteObjectRequest(destBucket, ResourcePath));
        } catch (AmazonServiceException ase) {
            logDetailsOfAmazonServiceException("Caught an AmazonServiceException, " + "which means your request made it "
                    + "to Amazon S3, but was rejected with an error " + "response for some reason.", ase);
            throw new DMCServiceException(DMCError.AWSError,
                    "AWS Delete Request from " + userEPPN + " made it, but rejected do to: " + ase.getMessage());
        } catch (AmazonClientException ace) {
            logAmazonClientException("Caught an AmazonClientException, which means the client encountered an internal error"
                            + " while trying to communicate with S3, such as not being able to access the network.", ace);
            throw new DMCServiceException(DMCError.AWSError, "AWS Delete Request from " + userEPPN
                    + " encountered internal error communication with " + "S3 and rejected do to: " + ace.getMessage());
        }
        return ResourcePath;

    }// Remove

    // Function to create a path to resource in S3 to store in DB for easy
    // future references
    public static String createPath(String URL) throws DMCServiceException {
        // Parse URL to get Path
        try {
            final int ResourcePathStart = URL.indexOf("com/") + 4;
            final int ResourcePathEnd = URL.indexOf("?A");
            final String ResourcePath = URL.substring(ResourcePathStart, ResourcePathEnd);
            return ResourcePath;
        } catch (Exception e) {
            throw new DMCServiceException(DMCError.AWSError,
                    "AWS create path from " + URL + "encountered internal error");
        }
    }// createPath

    public static String refreshURL(String path) throws DMCServiceException {
        final AmazonS3 s3client = getAmazonS3Client();

        ServiceLogger.log(LOGTAG, "Refreshing pre-signed URL.");

        // Parameters for Request, can change expiration time to any amount.
        final java.util.Date expiration = getExpirationTime();

        final GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(destBucket, path);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        final URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        final String preSignedURL = url.toString();
        return preSignedURL;
    }

    private static AmazonS3 getAmazonS3Client() {
        final BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        final AmazonS3 s3client = new AmazonS3Client(awsCreds);
        return s3client;
    }

    private static java.util.Date getExpirationTime() {
        final java.util.Date expiration = new java.util.Date();
        long milliSeconds = expiration.getTime();
        milliSeconds += 1000 * 60 * 60; // Add 1 hour.
        expiration.setTime(milliSeconds);
        return expiration;
    }

    // Helper function to check if timestamp expired
    public static boolean isTimeStampExpired(Timestamp expiration) {
        // create a java calendar instance
        final Calendar calendar = Calendar.getInstance();
        // get a java.util.Date from the calendar instance.
        final java.util.Date now = calendar.getTime();

        // a java current time (now) instance
        final java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        if (expiration.after(currentTimestamp)) {
            return true;
        }
        // should be false, but true for now for testing
        return false;
    }

    // Helper function to check Filename conventions
    private static boolean isFileNameValid(String filename) {

        // Future validation checks for security will go here
        if (filename == null || filename.length() == 0) {
            return false;
        }

        return true;
    }

    private static String createSourceKey(String tempURL) {
        // Convert temp URL to sourceKey
        final String sourceKey = tempURL.substring(tempURL.indexOf("com/") + 4, tempURL.length());

        // Remove bucket name
        return sourceKey.substring(sourceKey.indexOf('/') + 1, sourceKey.length());

    }
    private static String createDestKey(String tempURL, String Folder, String userEPPN, String ResourceType) throws DMCServiceException {
        // Create the destPath
        final String destPath = Folder + "/" + userEPPN + "/" + ResourceType;

        // Throws error if source path invalid
        if (!isFileNameValid(destPath)) {
            // NEED to add DMC method for AWS exceptions
            throw new DMCServiceException(DMCError.AWSError, "Source path from" + userEPPN + "is invalid ");
        }

        final String filename = tempURL.substring(tempURL.lastIndexOf('/') + 1, tempURL.length());

        // Throws error if file invalid
        if (!isFileNameValid(filename)) {
            throw new DMCServiceException(DMCError.AWSError, "File from" + userEPPN + "is invalid ");
        }

        return createDestKeyFromPathAndFilename(destPath, filename);
    }
    // helper function to create DestKeys
    private static String createDestKeyFromPathAndFilename(String destPath, String filename) {

        // Using a General Hash Code Function for now. Future hash functions
        // should generated unique hashes
        final String hashCode = Integer.toString(filename.hashCode() % 1000000);

        // Get current time from system
        final long unixTime = System.currentTimeMillis() / 1000L;

        // String Concatenation for location // + destPath + "/" +
        final String destKey = destPath + "/" + unixTime + "-" + hashCode + "-sanitized-" + filename;

        return destKey;
    }

    private static void logAmazonClientException(String msg, AmazonClientException ace) {
        ServiceLogger.log(LOGTAG, msg);
        ServiceLogger.log(LOGTAG, "Error Message: " + ace.getMessage());
    }

    private static void logDetailsOfAmazonServiceException(String msg, AmazonServiceException ase) {
        ServiceLogger.log(LOGTAG, msg);
        // Detailed Error Logging
        ServiceLogger.log(LOGTAG, "Error Message:    " + ase.getMessage());
        ServiceLogger.log(LOGTAG, "HTTP Status Code: " + ase.getStatusCode());
        ServiceLogger.log(LOGTAG, "AWS Error Code:   " + ase.getErrorCode());
        ServiceLogger.log(LOGTAG, "Error Type:       " + ase.getErrorType());
        ServiceLogger.log(LOGTAG, "Request ID:       " + ase.getRequestId());
    }

}// End class
