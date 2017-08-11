package org.dmc.services.esignature;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.LinkedHashMap;

// import javax.annotation.PostConstruct;

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
// import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
// import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
// import java.net.URL;
// import java.net.URLEncoder;
// import java.net.HttpURLConnection;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.Proxy;
// import java.net.InetSocketAddress;
// import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;

import org.dmc.services.data.entities.ESignDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ESignModel;
import org.dmc.services.data.repositories.ESignRepository;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.dmc.services.security.UserPrincipal;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ESignService {

	private String eSignURL = "https://api.pdffiller.com/v2/fillable_forms";

  @Value("${ESIGN_KEY}")
	private String eSignKey;

  @Value("${DOCUMENT_ID}")
	private String eDocuID;

	@Inject
	private ESignRepository esignRepository;

	// @Transactional
	// public ESignModel save(ESignModel esignModel) {
	// 		Mapper<ESignDocument, ESignModel> mapper = mapperFactory.mapperFor(ESignDocument.class, ESignModel.class);
	//
	// 		ESignDocument esignEntity = mapper.mapToEntity(esignModel);
	//
	// }

	private String getCompanyName(String CompanyInfo){

		try{
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = new HashMap<String, Object>();
				map = mapper.readValue(CompanyInfo, new TypeReference<Map<String, Object>>(){});
				return map.get("companyName").toString();
		} catch (Exception e) {
				// No-op
				e.printStackTrace();
				return "Unknown";
		}

	}

  public String eSignField(String CompanyInfo){

		CloseableHttpResponse response = null;
		InputStream is = null;
		String results = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String companyName = getCompanyName(CompanyInfo);
		// Integer id = (Integer) ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		// System.out.println("id" + id);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();

		try{
				HttpPost httppost = new HttpPost(eSignURL);
				httppost.addHeader("content-type", "application/json");
				httppost.addHeader("authorization", "Bearer " + eSignKey);

				String json =
				"{\"document_id\": \""+ eDocuID+ "\", " +
				"\"access\": \"full\", " +
				"\"name\": \"Membership Agreement Clean Version 3.4_"+ companyName + "_" + dtf.format(localDate) + "\", " +
				"\"status\": \"public\", "+
				"\"name_required\": false, "+
				"\"email_required\": true, "+
				"\"allow_downloads\": true, "+
				// "\"custom_logo_id\": 44947, "+
				"\"redirect_url\": \"https://dev-web2.opendmc.org/company-onboarding.php/pay\", "+
				"\"callback_url\": \"https://requestb.in/y82ufvy8\", "+
				"\"reusable\": false, "+
				"\"required_fields\": true, "+
				"\"signature_stamp\": false, "+
				"\"fillable_fields\":"  + CompanyInfo + "}";

				StringEntity reqEntity = new StringEntity(json);

				httppost.setEntity(reqEntity);
				// System.out.println("httppost " + httppost);
				// System.out.println("json " + json);
				// System.out.println("reqEntity " + reqEntity.getContent());

				response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				System.out.println("entity" + entity);
				if (entity != null) {
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
					httpget.addHeader("content-type", "application/json");
					httpget.addHeader("authorization", "Bearer " + eSignKey);

					response = httpclient.execute(httpget);

					HttpEntity entity = response.getEntity();
					if (entity != null) {
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

	public String eSignCallback(String DocumentID){
			return "";
	}
}
