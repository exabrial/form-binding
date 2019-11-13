package com.github.exabrial.formbinding.impl;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.github.exabrial.formbinding.FormBindingParam;
import com.github.exabrial.formbinding.FormBindingTransient;
import com.github.exabrial.formbinding.FormBindingWriter;

@SuppressWarnings("unchecked")
public class DefaultFormBindingWriter implements FormBindingWriter {
	private static final Pattern JAVA_FIELD_PATTERN = Pattern.compile("^[a-z_][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);

	@Override
	public String write(Object object) {
		Set<Field> boundFields = extractBoundFields(object);
		Map<String, String> values = convertToMap(object, boundFields);
		String result = convertMapToString(values);
		return result;
	}

	private String convertMapToString(Map<String, String> values) {
		try {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(values.size());
			for (String key : values.keySet()) {
				formParams.add(new BasicNameValuePair(key, values.get(key)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			formEntity.writeTo(baos);
			String result = baos.toString("UTF-8");
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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

	private Set<Field> extractBoundFields(Object object) {
		Set<Field> boundFields = getAllFields(object.getClass(), withAnnotation(FormBindingParam.class));
		if (boundFields.size() == 0) {
			boundFields = getAllFields(object.getClass());
		}
		Set<Field> transientFields = getAllFields(object.getClass(), withAnnotation(FormBindingTransient.class));
		boundFields.removeAll(transientFields);
		return boundFields;
	}

	private String extractKey(Field field) {
		FormBindingParam formBindingParam = field.getAnnotation(FormBindingParam.class);
		String key = null;
		if (formBindingParam != null) {
			key = formBindingParam.paramName();
			key = key.trim();
			if (key == "") {
				key = null;
			} else {
				if (!JAVA_FIELD_PATTERN.matcher(key).matches()) {
					throw new RuntimeException("Param name must match regex:" + JAVA_FIELD_PATTERN);
				}
			}
		}
		if (key == null) {
			key = field.getName();
		}
		return key;
	}

	private String extractValue(Object object, Field field) {
		try {
			field.setAccessible(true);
			Object value = field.get(object);
			if (value != null) {
				return value.toString();
			} else {
				return null;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
