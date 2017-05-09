package com.washinflash.rest.webservice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.washinflash.common.object.request.ImageRequest;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.rest.businessservice.ApplicationService;


@RestController
@RequestMapping("/rest/image")
public class ImageWebService {

	private static final Logger log = Logger.getLogger(ImageWebService.class);
	
	@Autowired
	private ApplicationService applicationService;
	
	
	@RequestMapping(value = "/getImageFile", method = RequestMethod.POST, consumes = "application/json", produces = {"image/jpg", "image/jpeg", "image/png"})
	public @ResponseBody byte[] getImageFile(@RequestBody(required = true) String reqJsonString)  {
		try {
			applicationService.checkAuthenticity(reqJsonString);
			ImageRequest imageRequest = (ImageRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ImageRequest.class);
			String fileName = imageRequest.getImageName();
			String extension = fileName.trim().split("\\.")[1];
			
			BufferedImage img = ImageIO.read(this.getClass().getResourceAsStream("/" + fileName.trim()));
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ImageIO.write(img, extension, bao);

			return bao.toByteArray();
		} catch (Throwable e) {
			log.debug(e.getMessage() + " --> for request string --> " + reqJsonString);
			return null;
		}
	}
}
