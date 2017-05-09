package com.washinflash.common.helper;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.washinflash.common.object.model.Message;

public class EmailHelper {

	private static final Logger log = Logger.getLogger(EmailHelper.class);

	public boolean sendEmail(String emailFrom, String emaiTo, String emailSub, String emailBody) {
		
		Boolean success = true;
		
		Message message = new Message();
		message.setMessageFrom(emailFrom);
		message.setMessageTo(emaiTo);
		message.setMessageSubject(emailSub);
		message.setMessageBody(emailBody);
		
		ClientResponse res = sendEmail(message);
		
		if(res != null && res.getStatus() != 200) {
			success = false;
			log.error("Failed to send email. " + res.toString());
		}
		
		return success;
	}
	
	public ClientResponse sendEmail(Message msg) {

		String domainName = "sandboxf661c8ebc3004e62af323457435f9e7d.mailgun.org";
		String  privateKey = "key-d541a30493cd560012fabc60713a2ea8";
		ClientResponse response = null;

		try{
		
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", privateKey));
		WebResource webResource =
				client.resource("https://api.mailgun.net/v3/" + domainName + "/messages");
		
		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		formData.add("from", msg.getMessageFrom());
		formData.add("to", msg.getMessageTo());
		formData.add("subject", msg.getMessageSubject());
		formData.add("text", msg.getMessageBody());
		
		response =  webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
				post(ClientResponse.class, formData);
		} catch (Exception e) {
			log.error("Failed to send email. " + e.getMessage() + e);
		}
		
		return response;
	}

}
