package org.dmc.services.esignature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.io.StringWriter;
import org.dmc.services.ServiceLogger;
// import org.apache.http.Header;
// import com.mysema.query.jpa.JPASubQuery;
// import com.mysema.query.types.Predicate;
// import com.mysema.query.types.query.ListSubQuery;

import org.dmc.services.data.entities.ESignDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ESignModel;
import org.dmc.services.data.repositories.ESignRepository;
import javax.inject.Inject;
import org.springframework.security.core.context.SecurityContextHolder;

import org.json.JSONObject;
import org.json.JSONException;

//Service for ESignService.
@Service
public class ESignService {

	private final String logTag = esignController.class.getName();

	//eSignURL
	private String eSignURL = "https://api.pdffiller.com/v2/fillable_forms";

	//Get value of API key from apache. If regenerated, remember to change on REST Machine as well
  @Value("${ESIGN_KEY}")
	private String eSignKey;

	//Get value for template id from apache. If changed, remember to change on REST Machine as well
  @Value("${ESIGN_DOCUMENT_ID}")
	private String eDocuID;

	//Commented for future if signing information need to be in database.
	// @Inject
	// private ESignRepository esignRepository;

	//Called by esignController.signDocument()
	//Generate Membership Agreement with pre-populate fields using apache HttpClients
  public String eSignField(String CompanyInfo){

		CloseableHttpResponse response = null;
		InputStream is = null;
		String results = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		//Jsonify company information.
		JSONObject jsonObj = new JSONObject(CompanyInfo);
		ServiceLogger.log(logTag, "Parsed companyInfo : " + jsonObj);
		String companyName = jsonObj.getString("companyName");

		//Get local date for document name.
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();

		try{
				HttpPost httppost = new HttpPost(eSignURL);

				//Add header for post request
				httppost.addHeader("content-type", "application/json");
				httppost.addHeader("authorization", "Bearer " + eSignKey);

				//Create json string for body request
				String json =
				"{\"document_id\":" + eDocuID + ", " +
				"\"access\": \"full\", " +
				"\"name\": \"Membership Agreement Clean Version 3.4_"+ companyName + "_" + dtf.format(localDate) + "\", " +
				"\"enforce_required_fields\": true, "+
				"\"status\": \"public\", "+
				"\"name_required\": true, "+
				"\"email_required\": true, "+
				"\"allow_downloads\": true, "+
				"\"custom_logo_id\": 44665, "+
				"\"custom_message\": \"Please go to PAGE 51 to sign the agreement\", " +
				"\"redirect_url\": \"https://dev-web2.opendmc.org/company-onboarding.php#/pay\", "+
				// "\"callback_url\": \"https://requestb.in/y82ufvy8\", "+
				"\"reusable\": false, "+
				"\"signature_stamp\": false, "+
				"\"fillable_fields\":"  + jsonObj + "}";

				//Make json string as entity for request body
				StringEntity reqEntity = new StringEntity(json);
				httppost.setEntity(reqEntity);

				//Execute request
				response = httpclient.execute(httppost);

				//Get response
				HttpEntity entity = response.getEntity();
				System.out.println("entity" + entity);
				if (entity != null) {
						//Stringify response & return to controller
						is = entity.getContent();
						StringWriter writer = new StringWriter();
						IOUtils.copy(is, writer, "UTF-8");
						results = writer.toString();
				}

				try {
						if (is != null) {
								is.close();
						}
				} catch (Exception e) {
						// Exception handler
						e.printStackTrace();
			      return null;
				}

				try {
						if (response != null) {
								response.close();
						}
				} catch (Exception e) {
						// Exception handler
						e.printStackTrace();
			      return null;
				}

				httpclient.close();
				return results;
		}catch (Exception e) {
				// Exception handler
				e.printStackTrace();
				return null;
		}
  }

	//Called by esignController.signCheck(),
	//Check signaure information using apache HttpClients
	public String eSignCheck(String DocumentID){

			CloseableHttpResponse response = null;
			InputStream is = null;
			String results = null;
			CloseableHttpClient httpclient = HttpClients.createDefault();

			try{
					//Pass template id to url
					HttpGet httpget = new HttpGet(eSignURL + '/' +DocumentID + "/filled_forms");

					ServiceLogger.log(logTag, "eSignCheck url: " + eSignURL + '/' + DocumentID + "/filled_forms");

					//Add header
					httpget.addHeader("content-type", "application/x-www-form-urlencoded");
					httpget.addHeader("authorization", "Bearer " + eSignKey);

					//Execute http request
					response = httpclient.execute(httpget);

					//Get response
					HttpEntity entity = response.getEntity();
					if (entity != null) {
							//Stringify response & return
							is = entity.getContent();
							StringWriter writer = new StringWriter();
							IOUtils.copy(is, writer, "UTF-8");
							results = writer.toString();
							ServiceLogger.log(logTag, "eSignCheck results: " + results);
					}

					try {
							if (is != null) {
									is.close();
							}
					} catch (Exception e) {
							// Exception handler
							e.printStackTrace();
				      return null;
					}

					try {
							if (response != null) {
									response.close();
							}
					} catch (Exception e) {
							// Exception handler
							e.printStackTrace();
				      return null;
					}

					httpclient.close();
					return results;
			}catch (Exception e) {
					// Exception handler
					e.printStackTrace();
					return "Unable to call filled_forms API";
			}
	}

	//Called by esignController.signToken(),
	//Create token with userEPPN for signature verification
	public String eSignToken(String userEPPN){
			//Token creation url
			String eSignTokenURL = "https://api.pdffiller.com/v2/tokens";

			CloseableHttpResponse response = null;
			InputStream is = null;
			String results = null;
			CloseableHttpClient httpclient = HttpClients.createDefault();

			try{
					HttpPost httppost = new HttpPost(eSignTokenURL);

					//Add header
					httppost.addHeader("content-type", "application/json");
					httppost.addHeader("authorization", "Bearer " + eSignKey);

					//Add json string with userEPPN
					String json =
					"{\"data\":" + "{\"userEPPN\": \"" + userEPPN + "\"}}";

					System.out.println(json);

					//Set body
					StringEntity reqEntity = new StringEntity(json);
					httppost.setEntity(reqEntity);

					//Execute HTTP request
					response = httpclient.execute(httppost);

					//Get response
					HttpEntity entity = response.getEntity();

					if (entity != null) {
							//Stringify response & return
							is = entity.getContent();
							StringWriter writer = new StringWriter();
							IOUtils.copy(is, writer, "UTF-8");
							results = writer.toString();
							ServiceLogger.log(logTag, "eSignToken results: " + results);
					}

					try {
							if (is != null) {
									is.close();
							}
					} catch (Exception e) {
							// Exception handler
							e.printStackTrace();
				      return null;
					}

					try {
							if (response != null) {
									response.close();
							}
					} catch (Exception e) {
							// Exception handler
							e.printStackTrace();
				      return null;
					}

					httpclient.close();
					return results;

			}catch (Exception e) {
					// Exception handler
					e.printStackTrace();
					return null;
			}
	}

	// Comment /rest/esignCallback if needed in future
	// public String eSignCallback(String DocumentID){
	// 		return "";
	// }
}
