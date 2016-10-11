package org.dmc.services.serviceRunTests;

/*import java.io.BufferedReader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;*/
import java.io.IOException;
import java.io.InputStream;
/*import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;*/
import java.util.HashMap;
import java.util.Map;

import org.dmc.services.ServiceLogger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;



public class ServiceRunFileUploadTest {
	private static final String LOGTAG = ServiceRunFileUploadTest.class.getName();
	
//	public static void main(String[] args)
	public static void test(String[] args)
	{
		//String modelString = "{\"interFace\":{\"version\":1,\"modelId\":\"aff647da-d82f-1004-8e7b-5de38b2eeb0f\",\"interfaceId\":\"aff647db-d82f-1004-8e7b-5de38b2eeb0f\",\"type\":\"interface\",\"name\":\"Default Interface\",\"path\":[30]},\"inParams\":{\"SpecimenWidth\":{\"name\":\"SpecimenWidth\",\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":3,\"parameterid\":\"d9f30f3a-d800-1004-8f53-704dbfababa8\"},\"CrackLength\":{\"name\":\"CrackLength\",\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":1,\"parameterid\":\"d9f30f37-d800-1004-8f53-704dbfababa8\"}},\"outParams\":{\"Alpha\":{\"name\":\"Alpha\",\"type\":\"Real\",\"unit\":\"no unit\",\"category\":\"no unit\",\"value\":0.3333333333333333,\"parameterid\":\"d9f30f3d-d800-1004-8f53-704dbfababa8\",\"instancename\":\"Alpha\"}},\"modelName\":\"Default Interface\",\"modelDescription\":\"\",\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}}";
		String modelString = "{\"interFace\":{\"version\":1,\"modelId\":\"9602f2b2-dd93-1004-8f9f-a350a3e7c039\",\"interfaceId\":\"9602f2b3-dd93-1004-8f9f-a350a3e7c039\",\"type\":\"interface\",\"name\":\"Default Interface\",\"path\":[43]},\"inParams\":{\"inFile\":{\"name\":\"inFile\",\"type\":\"File\",\"unit\":\"\",\"value\":\"inFile\",\"parameterid\":\"a3891106-dd93-1004-894e-fd7e2ea7b713\"}},\"outParams\":{\"outFile\":{\"name\":\"outFile\",\"type\":\"File\",\"unit\":\"\",\"value\":\"inFile\",\"parameterid\":\"a3891109-dd93-1004-894e-fd7e2ea7b713\",\"instancename\":\"outFile\"}},\"modelName\":\"Default Interface\",\"modelDescription\":\"\",\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}}";
		//String modelString = "{\"interFace\":{\"version\":1,\"modelId\":\"bd85f846-d8f4-1004-8f94-37c24b788523\",\"interfaceId\":\"bd85f847-d8f4-1004-8f94-37c24b788523\",\"type\":\"interface\",\"name\":\"Upload a file interface\",\"path\":[31]},\"inParams\":{\"localPath\":{\"name\":\"localPath\",\"type\":\"String\",\"unit\":\"\",\"value\":\"C:/tmp/Out\",\"parameterid\":\"73185a14-d398-1004-8645-569b82669417\"},\"inFile\":{\"name\":\"inFile\",\"type\":\"File\",\"unit\":\"\",\"value\":\"0\",\"parameterid\":\"73185a12-d398-1004-8645-569b82669417\"}},\"outParams\":{\"outFilename\":{\"name\":\"outFilename\",\"type\":\"String\",\"unit\":\"\",\"value\":\"0\",\"parameterid\":\"73185a16-d398-1004-8645-569b82669417\",\"instancename\":\"outFilename\"}},\"modelName\":\"Upload a file interface\",\"modelDescription\":\"\",\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}}";
		//		String parameters = "{\"interFace\":{\"version\":1,\"modelId\":\"aff647da-d82f-1004-8e7b-5de38b2eeb0f\",\"interfaceId\":\"aff647db-d82f-1004-8e7b-5de38b2eeb0f\",\"type\":\"interface\",\"name\":\"Default Interface\",\"path\":[30]},\"inParams\":{\"SpecimenWidth\":{\"name\":\"SpecimenWidth\",\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":3,\"parameterid\":\"d9f30f3a-d800-1004-8f53-704dbfababa8\"},\"CrackLength\":{\"name\":\"CrackLength\",\"type\":\"Real\",\"unit\":\"meter\",\"category\":\"length\",\"value\":1,\"parameterid\":\"d9f30f37-d800-1004-8f53-704dbfababa8\"}},\"outParams\":{\"Alpha\":{\"name\":\"Alpha\",\"type\":\"Real\",\"unit\":\"no unit\",\"category\":\"no unit\",\"value\":0.3333333333333333,\"parameterid\":\"d9f30f3d-d800-1004-8f53-704dbfababa8\",\"instancename\":\"Alpha\"}},\"modelName\":\"Default Interface\",\"modelDescription\":\"\",\"server\":{\"name\":\"localhost\",\"port\":\"7795\",\"user\":\"ceed\",\"pw\":\"ceed\",\"space\":\"USER\"}}";
		//String queueID = "TestQueue";
//		MultipartFile mf = new MockMultipartFile("infile", "myContent1".getBytes());

		
		//String servicePath = "http://52.43.100.191:8080/DOMEApiServicesV7/runModel";
		//String servicePath = "http://localhost:8080/DOMEApiServicesV7/runModel";
		String servicePath = "http://localhost:8080/DOME/runModel";
		

		ServiceRunFileUploadTest instance = new ServiceRunFileUploadTest();

		HashMap<String,String> parameters = new HashMap<String,String>();
		String queueID = "Test" + "_"
				+ System.currentTimeMillis();

		parameters.put("data", modelString);
		parameters.put("queue", queueID);
		HashMap<String,String> files = new HashMap<String,String>();
		files.put("inFile", "c:/tmp/In/test.txt");
		
		
		try {
		post(servicePath,parameters,files);
		//post(servicePath,parameters,null);
		System.out.println("Done");
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}
	
	public static String post(String postUrl, Map<String, String> params,
            Map<String, String> files) throws ClientProtocolException,
            IOException {
        CloseableHttpResponse response = null;
        InputStream is = null;
        String results = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {

            HttpPost httppost = new HttpPost(postUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            if (params != null) {
                for (String key : params.keySet()) {
                    StringBody value = new StringBody(params.get(key),
                    		ContentType.TEXT_PLAIN);
                            //ContentType.MULTIPART_FORM_DATA);
                    		//ContentType.APPLICATION_OCTET_STREAM);
                    		//ContentType.TEXT_PLAIN);
                    builder.addPart(key, value);
                }
            }

            if (files != null && files.size() > 0) {
                for (String key : files.keySet()) {
                    String value = files.get(key);
                    FileBody body = new FileBody(new File(value));
                    builder.addPart(key, body);
                }
            }
            
            //builder.setBoundary("bbbb");
            HttpEntity reqEntity = builder.build();
           
            httppost.setEntity(reqEntity);

            response = httpclient.execute(httppost);
            // assertEquals(200, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                results = writer.toString();
            }

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            httpclient.close();
        }

        return results;
    }

    public static String get(String getStr) throws IOException,
            ClientProtocolException {
        CloseableHttpResponse response = null;
        InputStream is = null;
        String results = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(getStr);
            response = httpclient.execute(httpGet);

            //assertEquals(200, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                results = writer.toString();
            }

        } finally {

            try {
                if (is != null) {
                    is.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (Throwable t) {
                // No-op
            }

            httpclient.close();
        }

        return results;
    }
}
