package com.github.exabrial.formbinding.impl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.exabrial.formbinding.FormBindingConverter;
import com.github.exabrial.formbinding.FormBindingParam;
import com.github.exabrial.formbinding.FormBindingTransient;

class CommonCode {
	private static final Pattern JAVA_FIELD_PATTERN = Pattern.compile("^[a-z_][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);
	public static final ConvertUtilsBean cub;

	static {
		cub = new ConvertUtilsBean();
		final Converter existingStringConverter = cub.lookup(String.class);
		final ServiceLoader<FormBindingConverter> serviceLoader = ServiceLoader.load(FormBindingConverter.class);
		for (final FormBindingConverter converter : serviceLoader) {
			System.out.println("FormBindingConverter Registered:" + converter.getClass().getSimpleName());
			final Converter delegatingConverter = new Converter() {
				@Override
				public <T> T convert(final Class<T> type, final Object value) {
					if (type.equals(converter.targetClass()) || value.getClass().equals(converter.targetClass())) {
						return converter.convert(type, value);
					} else {
						return existingStringConverter.convert(type, value);
					}
				}
			};
			cub.register(delegatingConverter, converter.targetClass());
			cub.register(delegatingConverter, String.class);
		}
	}

	private CommonCode() {
	}

	static Set<Field> extractBoundFields(final Class<?> clazz) {

		final Set<Field> boundFields = new HashSet<>(FieldUtils.getFieldsListWithAnnotation(clazz, FormBindingParam.class));
		if (boundFields.size() == 0) {
			boundFields.addAll(FieldUtils.getAllFieldsList(clazz));
		}
		final Set<Field> transientFields = new HashSet<>(FieldUtils.getFieldsListWithAnnotation(clazz, FormBindingTransient.class));
		boundFields.removeAll(transientFields);
		return boundFields;
	}

	static String extractKey(final Field field) {
		final FormBindingParam formBindingParam = field.getAnnotation(FormBindingParam.class);
		String key = null;
		if (formBindingParam != null) {
			key = formBindingParam.paramName();
			key = key.trim();
			if (key.equals("")) {
				key = null;
			} else if (!JAVA_FIELD_PATTERN.matcher(key).matches()) {
				throw new RuntimeException("Param name must match regex:" + JAVA_FIELD_PATTERN);
			}
		}
		if (key == null) {
			key = field.getName();
		}
		return key;
	}
}
