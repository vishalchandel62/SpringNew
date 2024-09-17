package com.credentek.msme.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;


public class VerifyRecaptcha {
	
	private final static Logger log = LogManager.getLogger(VerifyRecaptcha.class);
	public static boolean verify(String gRecaptchaResponse) throws IOException {
		
		//log.info("::::CAPTCHA_URL::::::::");
	//	log.info("::::CAPTCHA_SECREAT_KEY::::::::");
		boolean captchaFlag = false;
	//	log.info("calling verify method::::::<<<<<<<<<:::");
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		
		try
		{
			URL obj = new URL(ApplicationGenParameter.CAPTCHA_URL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			// add reuqest header
			con.setRequestMethod("POST");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	
			String postParams = "secret=" + ApplicationGenParameter.CAPTCHA_SECREAT_KEY + "&response="
					+ gRecaptchaResponse;
	
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();


			int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'POST' request to URL : " + postParams);
			log.info("Post parameters : ");
			log.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			log.info("captcha response:::");
			
			//parse JSON response and return 'success' value
			JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();
			
			//return jsonObject.getBoolean("success");
			//log.info("success response::::"+jsonObject.getBoolean("success"));
			
			if(jsonObject.getBoolean("success")){
				
				captchaFlag = true;
			}
		
		}catch(Exception e){
			log.info("Exception:::",e);
		}
		return captchaFlag;
	}
	

}
