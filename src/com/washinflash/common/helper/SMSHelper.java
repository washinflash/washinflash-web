package com.washinflash.common.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.washinflash.common.object.model.Message;
import com.washinflash.common.util.GenericConstant;


public class SMSHelper {

	private static final Logger log = Logger.getLogger(SMSHelper.class);

	public boolean sendSMS(String fromMob, String toMob, String sms) {
		
		Message message = new Message(); 
		message.setMessageTo(toMob);
		message.setMessageFrom(fromMob);
		message.setMessageBody(sms);
		
		boolean smsSuccess = sendSMS(message);
		
		return smsSuccess;
	}
	
	public boolean sendSMS(Message msg) {

		boolean success = true;

		try {

			//Your authentication key
			String authkey = "107206AxkLtSTfPB56e14a17";
			//For transactional message
			String route = "4";
			String encodedMessage = URLEncoder.encode(msg.getMessageBody(), GenericConstant.ENCODE_UTF8);

			//Send SMS API
			String mainUrl="https://control.msg91.com/api/sendhttp.php?";

			//Prepare parameter string 
			StringBuilder sbPostData= new StringBuilder(mainUrl);
			sbPostData.append("authkey=" + authkey); 
			sbPostData.append("&mobiles=" + msg.getMessageTo());
			sbPostData.append("&message=" + encodedMessage);
			sbPostData.append("&route=" + route);
			sbPostData.append("&sender=" + msg.getMessageFrom());

			//final string
			mainUrl = sbPostData.toString();

			//Prepare Url
			URL myURL = null;
			URLConnection myURLConnection = null;		
			BufferedReader reader = null;

			//prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			//reading response 
			String response = "";
			String line;
			while ((line = reader.readLine()) != null) {
				response += line;
			}
			//print response 
			log.debug(response);
			//finally close connection
			reader.close();
		} catch (Exception e) { 
			success = false;
			log.error(e.getMessage() + e);
		}		

		return success;
	}

}