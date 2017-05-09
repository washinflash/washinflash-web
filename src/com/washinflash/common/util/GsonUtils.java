package com.washinflash.common.util;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.washinflash.common.exception.SystemException;

public final class GsonUtils {

	private static final Logger log = Logger.getLogger(GsonUtils.class);
	
	private static final Gson gson = new Gson();

	@SuppressWarnings("unchecked")
	public static Object getObjectFromJsonString (String requestJson, Class className) throws SystemException {

		Object responseObject = null;
		try {
			responseObject = gson.fromJson(requestJson, className);
		} catch (JsonSyntaxException jsonEx) {
			log.error("Invalid JSON string." + jsonEx);
			throw new SystemException(StatusConstant.FAILED,"Invalid JSON string.", jsonEx);
		}
		return responseObject;
	}

	public static String toJsonString(Object obj) {

		return gson.toJson(obj);
	}

}
