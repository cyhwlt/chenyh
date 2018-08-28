package com.springboot.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	/**
	 * Json ObjectMapper
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String objectToJson(Object obj){
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	/**
	 *  JSON 转换指定 JAVA 对象
	 * @param jsonStr
	 * @param objClass
	 * @param <T>
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr,Class<T> objClass){
		try {
			return objectMapper.readValue(jsonStr,objClass);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  JSON 转换指定 JAVA 对象   抛出异常
	 * @param jsonStr
	 * @param objClass
	 * @param <T>
	 * @return
	 */
	public static <T> T jsonToObjectException(String jsonStr,Class<T> objClass) throws IOException {
		return objectMapper.readValue(jsonStr,objClass);
	}

	/**
	 * JSON 转换指定 JAVA 对象
	 * @param jsonStr
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr,TypeReference type){
		try {
			return objectMapper.readValue(jsonStr,type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
