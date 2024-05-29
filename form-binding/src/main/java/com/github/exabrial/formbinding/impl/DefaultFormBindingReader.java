package com.github.exabrial.formbinding.impl;

import static com.github.exabrial.formbinding.impl.CommonCode.extractBoundFields;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.github.exabrial.formbinding.FormBindingReader;

public class DefaultFormBindingReader implements FormBindingReader {

	@Override
	public <ReturnType> ReturnType read(String input, final Class<ReturnType> returnTypeClazz) {
		if (returnTypeClazz != null && input != null) {
			input = input.trim();
			try {
				final Set<Field> boundFields = extractBoundFields(returnTypeClazz);
				final ReturnType returnValue = returnTypeClazz.newInstance();
				final Map<String, String> valueMap = splitQuery(input);
				for (final String key : valueMap.keySet()) {
					final Field field = findMatchingField(key, boundFields);
					if (field != null) {
						field.setAccessible(true);
						final Class<?> type = field.getType();
						final Object object = CommonCode.cub.convert(valueMap.get(key), type);
						field.set(returnValue, object);
					}
				}
				return returnValue;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	private static Field findMatchingField(final String name, final Set<Field> boundFields) {
		try {
			return boundFields.stream().filter(element -> paramMatches(element, name)).findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	private static boolean paramMatches(final Field element, final String name) {
		final String key = CommonCode.extractKey(element);
		return key != null && key.equals(name);
	}

	private Map<String, String> splitQuery(String form) {
		if (form == null || (form = form.trim()).equals("")) {
			return Collections.emptyMap();
		} else {
			final Map<String, String> map = new HashMap<>();
			Arrays.stream(form.split("&")).map(this::splitQueryParameter).forEach(pair -> map.put(pair[0], pair[1]));
			return map;
		}
	}

	private String[] splitQueryParameter(final String it) {
		final String[] split = it.split("=");
		String value;
		if (split.length > 1) {
			value = decode(split[1]);
		} else {
			value = null;
		}
		return new String[] { split[0], value };
	}

	private String decode(final String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
