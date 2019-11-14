package com.github.exabrial.formbinding.impl;

import static com.github.exabrial.formbinding.impl.CommonCode.extractBoundFields;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

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
			List<NameValuePair> formParams = URLEncodedUtils.parse(input, StandardCharsets.UTF_8);
			Set<Field> boundFields = extractBoundFields(returnTypeClazz);
			ReturnType returnValue = returnTypeClazz.newInstance();
			for (NameValuePair pair : formParams) {
				String name = pair.getName();
				String value = pair.getValue();
				Field field = findMatchingField(name, boundFields);
				if (field != null) {
					field.setAccessible(true);
					Class<?> type = field.getType();
					Object object = cub.convert(value, type);
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
}
