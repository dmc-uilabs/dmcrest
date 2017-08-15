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
// import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.io.StringWriter;
import org.dmc.services.ServiceLogger;
import org.apache.http.Header;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;

import org.dmc.services.data.entities.ESignDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ESignModel;
import org.dmc.services.data.repositories.ESignRepository;
import javax.inject.Inject;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.dmc.services.security.UserPrincipal;

import org.json.JSONObject;
import org.json.JSONException;

@Service
public class ESignService {

	private final String logTag = esignController.class.getName();

	private String eSignURL = "https://api.pdffiller.com/v2/fillable_forms";

  @Value("${ESIGN_KEY}")
	private String eSignKey;

  @Value("${ESIGN_DOCUMENT_ID}")
	private String eDocuID;

	@Inject
	private ESignRepository esignRepository;

  public String eSignField(String CompanyInfo){

		CloseableHttpResponse response = null;
		InputStream is = null;
		String results = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		ServiceLogger.log(logTag, "eSignField url: " + eSignURL);

		JSONObject jsonObj = new JSONObject(CompanyInfo);
		String companyName = jsonObj.getString("companyName");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();

		try{
				HttpPost httppost = new HttpPost(eSignURL);
				httppost.addHeader("content-type", "application/json");
				httppost.addHeader("authorization", "Bearer " + eSignKey);

				String json =
				"{\"document_id\":" + eDocuID + ", " +
				"\"access\": \"full\", " +
				"\"name\": \"Membership Agreement Clean Version 3.4_"+ companyName + "_" + dtf.format(localDate) + "\", " +
				"\"status\": \"public\", "+
				"\"name_required\": false, "+
				"\"email_required\": true, "+
				"\"allow_downloads\": true, "+
				// "\"custom_logo_id\": 44947, "+
				"\"redirect_url\": \"https://dev-web2.opendmc.org/company-onboarding.php#/pay\", "+
				// "\"callback_url\": \"https://requestb.in/y82ufvy8\", "+
				"\"reusable\": false, "+
				"\"required_fields\": true, "+
				"\"signature_stamp\": false, "+
				"\"fillable_fields\":"  + jsonObj + "}";

				StringEntity reqEntity = new StringEntity(json);

				httppost.setEntity(reqEntity);

				response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				System.out.println("entity" + entity);
				if (entity != null) {
						is = entity.getContent();
						StringWriter writer = new StringWriter();
						IOUtils.copy(is, writer, "UTF-8");
						results = writer.toString();
						ServiceLogger.log(logTag, "eSignField results: " + results);
				}

				try {
						if (is != null) {
								is.close();
						}
				} catch (Exception e) {
						// No-op
						e.printStackTrace();
			      return null;
				}

				try {
						if (response != null) {
								response.close();
						}
				} catch (Exception e) {
						// No-op
						e.printStackTrace();
			      return null;
				}

				httpclient.close();
				return results;
		}catch (Exception e) {
				// No-op
				e.printStackTrace();
				return null;
		}
  }

	public String eSignCheck(String DocumentID){

			CloseableHttpResponse response = null;
			InputStream is = null;
			String results = null;
			CloseableHttpClient httpclient = HttpClients.createDefault();

			try{
					HttpGet httpget = new HttpGet(eSignURL + '/' +DocumentID + "/filled_forms");

					ServiceLogger.log(logTag, "eSignCheck url: " + eSignURL + '/' + DocumentID + "/filled_forms");
					httpget.addHeader("content-type", "application/x-www-form-urlencoded");
					httpget.addHeader("authorization", "Bearer " + eSignKey);

					response = httpclient.execute(httpget);

					HttpEntity entity = response.getEntity();
					if (entity != null) {
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
							// No-op
							e.printStackTrace();
				      return null;
					}

					try {
							if (response != null) {
									response.close();
							}
					} catch (Exception e) {
							// No-op
							e.printStackTrace();
				      return null;
					}

					httpclient.close();
					return results;
			}catch (Exception e) {
					// No-op
					e.printStackTrace();
					return "Unable to call filled_forms API";
			}
	}

	public String eSignCallback(String DocumentID){
			return "";
	}
}
