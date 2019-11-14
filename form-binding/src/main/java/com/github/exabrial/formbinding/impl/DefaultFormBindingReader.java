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

import org.apache.commons.beanutils.ConvertUtilsBean;

import com.github.exabrial.formbinding.FormBindingReader;

public class DefaultFormBindingReader implements FormBindingReader {
	private final ConvertUtilsBean cub;

	public DefaultFormBindingReader() {
		cub = new ConvertUtilsBean();
		CommonCode.loadSpiConverters(cub);
	}

	@Override
	public <ReturnType> ReturnType read(String input, Class<ReturnType> returnTypeClazz) {
		try {
			Set<Field> boundFields = extractBoundFields(returnTypeClazz);
			ReturnType returnValue = returnTypeClazz.newInstance();
			Map<String, String> valueMap = splitQuery(input);
			for (String key : valueMap.keySet()) {
				Field field = findMatchingField(key, boundFields);
				if (field != null) {
					field.setAccessible(true);
					Class<?> type = field.getType();

					Object object = cub.convert(valueMap.get(key), type);
					field.set(returnValue, object);
				}
			}
			return returnValue;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static Field findMatchingField(String name, Set<Field> boundFields) {
		try {
			return boundFields.stream().filter(element -> paramMatches(element, name)).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	private static boolean paramMatches(Field element, String name) {
		String key = CommonCode.extractKey(element);
		return key != null && key.equals(name);
	}

	private Map<String, String> splitQuery(String form) {
		if (form == null || (form = form.trim()).equals("")) {
			return Collections.emptyMap();
		} else {
			Map<String, String> map = new HashMap<String, String>();
			Arrays.stream(form.split("&")).map(this::splitQueryParameter).forEach(pair -> map.put(pair[0], pair[1]));
			return map;
		}
	}

	private String[] splitQueryParameter(String it) {
		String[] split = it.split("=");
		String value;
		if (split.length > 1) {
			value = decode(split[1]);
		} else {
			value = null;
		}
		return new String[] { split[0], value };
	}

	private String decode(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
