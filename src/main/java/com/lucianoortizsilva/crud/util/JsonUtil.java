package com.lucianoortizsilva.crud.util;

import com.google.gson.Gson;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtil {

	public static String convertToJson(final Object object) {
		try {
			Gson gson = new Gson();
			return gson.toJson(object);
		} catch (Exception e) {
			log.error("[ERRO]", e);
		}
		return null;
	}
	
	
	public static Object convertToObject(final String json, final Class<?> object) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, object);
		} catch (Exception e) {
			log.error("[ERRO]", e);
		}
		return null;
	}

}