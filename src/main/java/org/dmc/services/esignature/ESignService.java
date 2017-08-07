package org.dmc.services.esignature;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;

@Service
public class ESignService {

	private String eSignURL = "https://api.pdffiller.com/v2/fillable_forms";

  @Value("${ESIGN_KEY}")
	private String eSignKey;

  @Value("${DOCUMENT_ID}")
	private String eDocuID;



  // private JSONObject formatSignBodyRequest(String CompanyInfo){
	//
  //     JSONObject bodyRequest = new JSONObject();
	// 		JSONParser parser = new JSONParser();
	// 		JSONObject CompanyJson = new JSONObject();
	// 		if (CompanyInfo != "")
	// 			CompanyJson = (JSONObject) parser.parse(CompanyInfo);
	// 		else
	// 			CompanyJson = null;
	//
  //     bodyRequest.put("document_id", eDocuID);
  //     bodyRequest.put("access", "full");
  //     bodyRequest.put("status", "public");
  //     bodyRequest.put("name_required", false);
  //     bodyRequest.put("email_required", true);
	// 		bodyRequest.put("allow_downloads", true);
	// 		bodyRequest.put("redirect_url", "https://dev-web2.opendmc.org/company-onboarding.php/pay");
	// 		bodyRequest.put("callback_url", "https://requestb.in/utv7drut");
	// 		bodyRequest.put("reusable", true);
	// 		bodyRequest.put("required_fields", true);
	// 		bodyRequest.put("fillable_fields", CompanyJson);
	//
	// 		return bodyRequest;
	// }

  public String eSignField(String CompanyInfo){

		try{
			 	URL obj = new URL(eSignURL);
			  HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
				conn.setRequestProperty("content-type", "application/json");
		    conn.setRequestProperty("authorization", eSignKey);
		    conn.setDoOutput(true);

		    conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);

				String urlParameters =
        "document_id=" + URLEncoder.encode(eDocuID, "UTF-8") +
        "&access=" + URLEncoder.encode("full", "UTF-8") +
				"&status=" + URLEncoder.encode("public", "UTF-8") +
				"&name_required=" + URLEncoder.encode("false", "UTF-8") +
				"&email_required=" + URLEncoder.encode("true", "UTF-8") +
				"&allow_downloads=" + URLEncoder.encode("true", "UTF-8") +
				"&redirect_url=" + URLEncoder.encode("https://dev-web2.opendmc.org/company-onboarding.php/pay", "UTF-8") +
				"&callback_url=" + URLEncoder.encode("https://requestb.in/utv7drut", "UTF-8") +
				"&reusable=" + URLEncoder.encode("true", "UTF-8") +
				"&required_fields=" + URLEncoder.encode("true", "UTF-8") +
				"&fillable_fields=" + URLEncoder.encode(CompanyInfo, "UTF-8");

				// JSONObject json = formatSignBodyRequest(CompanyInfo);

		    // String data =  "{\"format\":\"json\",\"pattern\":\"#\"}";
				//Send request
		    DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
		    wr.writeBytes (urlParameters);
		    wr.flush ();
		    wr.close ();

		    //Get Response
		    InputStream is = conn.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    StringBuffer response = new StringBuffer();
		    while((line = rd.readLine()) != null) {
		        response.append(line);
		        response.append('\r');
		    }
		    rd.close();
				conn.disconnect();
		    return response.toString();
		}catch (Exception e) {
				e.printStackTrace();
	      return null;
	  }
  }

	// public Charge createCharge(String token, String name) throws AuthenticationException,
	// 		InvalidRequestException, APIConnectionException, CardException, APIException {
  //
	// 	Map<String, Object> params = new HashMap<String, Object>();
	// 	params.put("amount", 50000);
	// 	params.put("currency", "usd");
	// 	params.put("description", "Example charge for " + name);
	// 	params.put("source", token);
	// 	//Will throw an exception if payment fails
	// 	return Charge.create(params);
  //
	// }
}
