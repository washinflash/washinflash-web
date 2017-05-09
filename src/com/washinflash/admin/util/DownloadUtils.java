package com.washinflash.admin.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DownloadUtils {
	
	private static final Logger log = Logger.getLogger(DownloadUtils.class);

	public void downloadFile(HttpServletResponse response, String contentName, byte[] bytesIn) {
		OutputStream ous = null;
		try {
			ous = response.getOutputStream();
			response.setContentType("text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + contentName);
			
			ous.write(bytesIn);
			
		} catch (IOException e) {
			log.error("Unable to read/write file content", e);
		} finally {
			if (ous != null) {
				try {
					ous.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
