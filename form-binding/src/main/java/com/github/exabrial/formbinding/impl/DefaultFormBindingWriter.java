package com.github.exabrial.formbinding.impl;

import static com.github.exabrial.formbinding.impl.CommonCode.extractBoundFields;
import static com.github.exabrial.formbinding.impl.CommonCode.extractKey;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.github.exabrial.formbinding.FormBindingWriter;

public class DefaultFormBindingWriter implements FormBindingWriter {
	private final ConvertUtilsBean cub;

	public DefaultFormBindingWriter() {
		cub = new ConvertUtilsBean();
		CommonCode.loadSpiConverters(cub);
	}

	@Override
	public String write(Object object) {
		Set<Field> boundFields = extractBoundFields(object.getClass());
		Map<String, String> values = convertToMap(object, boundFields);
		String result = convertMapToString(values);
		return result;
	}

	private static String convertMapToString(Map<String, String> values) {
		List<NameValuePair> formParams = new ArrayList<NameValuePair>(values.size());
		for (String key : values.keySet()) {
			formParams.add(new BasicNameValuePair(key, values.get(key)));
		}
		String result = URLEncodedUtils.format(formParams, StandardCharsets.UTF_8);
		return result;
	}

	private Map<String, String> convertToMap(Object object, Set<Field> boundFields) {
		Map<String, String> values = new HashMap<>();
		for (Field field : boundFields) {
			String key = extractKey(field);
			String value = extractValue(object, field);
			values.put(key, value);
		}
		return values;
	}

	private String extractValue(Object object, Field field) {
		try {
			field.setAccessible(true);
			Object value = field.get(object);
			if (value != null) {
				return (String) cub.convert(value, String.class);
			} else {
				return null;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
