package com.github.exabrial.formbinding.impl;

import static com.github.exabrial.formbinding.impl.CommonCode.extractBoundFields;
import static com.github.exabrial.formbinding.impl.CommonCode.extractKey;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.exabrial.formbinding.FormBindingWriter;

public class DefaultFormBindingWriter implements FormBindingWriter {
	@Override
	public String write(final Object object) {
		if (object == null) {
			return null;
		} else {
			final Set<Field> boundFields = extractBoundFields(object.getClass());
			final Map<String, String> values = convertToMap(object, boundFields);
			final String result = convertMapToString(values);
			return result;
		}
	}

	private Map<String, String> convertToMap(final Object object, final Set<Field> boundFields) {
		final Map<String, String> values = new HashMap<>();
		for (final Field field : boundFields) {
			final String key = extractKey(field);
			final String value = extractValue(object, field);
			if (value != null) {
				values.put(key, value);
			}
		}
		return values;
	}

	private String extractValue(final Object object, final Field field) {
		try {
			field.setAccessible(true);
			final Object value = field.get(object);
			if (value != null) {
				return CommonCode.cub.convert(value);
			} else {
				return null;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static String convertMapToString(final Map<String, String> map) {
		final StringBuilder sb = new StringBuilder();
		map.entrySet().stream().forEach(entry -> {
			try {
				(entry.getValue() == null ? sb.append(entry.getKey())
						: sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"))).append('&');
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		});
		sb.delete(sb.length() - 1, sb.length());
		return sb.toString();
	}
}
