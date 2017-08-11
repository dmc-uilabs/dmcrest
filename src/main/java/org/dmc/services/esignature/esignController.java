package org.dmc.services.esignature;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.dmc.services.data.models.eSignStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class esignController {

	@Autowired
	private ESignService eSignService;

	@RequestMapping(value = "/esignDoc", method = RequestMethod.POST)
	public ResponseEntity<eSignStatus> signDocument(@RequestBody String CompanyInfo) {

			String response = "";
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, String> resultMap = new HashMap<String, String>();
			String results = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignField(CompanyInfo);
					// System.out.println("response " + response);
					try{
						 map = mapper.readValue(response, new TypeReference<Map<String, Object>>(){});
						 resultMap.put("url", map.get("url").toString());
	 					 resultMap.put("template_id", map.get("fillable_form_id").toString());
						 results = new ObjectMapper().writeValueAsString(resultMap);
					}catch (Exception e) {
							e.printStackTrace();
				      return null;
					}
					return new ResponseEntity<eSignStatus>(new eSignStatus(results, "eSignature Successful!"), HttpStatus.OK);
  		} catch (Exception e) {
					return new ResponseEntity<eSignStatus>(new eSignStatus(null, "eSignature Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}

	@RequestMapping(value = "/esignCheck/{LinkToFillID}", method = RequestMethod.GET)
	public ResponseEntity<eSignStatus> signCheck(@PathVariable("LinkToFillID") String LinkToFillID) {

			String response = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignCheck(LinkToFillID);

					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> map = new HashMap<String, Object>();

					try{
						 map = mapper.readValue(response, new TypeReference<Map<String, Object>>(){});
					}catch (Exception e) {
							e.printStackTrace();
				      return null;
					}

					// System.out.println(map);
					return new ResponseEntity<eSignStatus>(new eSignStatus(String.valueOf(map.get("total")), "eSignCheck Successful!"), HttpStatus.OK);

  		} catch (Exception e) {
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "eSignCheck Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}

	@RequestMapping(value = "/esignCallback", method = RequestMethod.POST)
	public ResponseEntity<eSignStatus> signCallback(@RequestBody String documentID) {

			String response = "";

      try {
  				//Will throw an exception if esign fails
  				response = eSignService.eSignCallback(documentID);
					System.out.println("response " + response);
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Successful!"), HttpStatus.OK);
  		} catch (Exception e) {
					// System.out.println("failed");
					return new ResponseEntity<eSignStatus>(new eSignStatus(response, "esignCallback Failed!"), HttpStatus.BAD_REQUEST);
  		}
	}
}
