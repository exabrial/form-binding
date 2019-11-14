package com.github.exabrial.formbinding.impl;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;

import com.github.exabrial.formbinding.FormBindingConverter;
import com.github.exabrial.formbinding.FormBindingParam;
import com.github.exabrial.formbinding.FormBindingTransient;

@SuppressWarnings("unchecked")
class CommonCode {
	private static final Pattern JAVA_FIELD_PATTERN = Pattern.compile("^[a-z_][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);
	public static final ConvertUtilsBean cub;

	static {
		cub = new ConvertUtilsBean();
		Converter existingStringConverter = cub.lookup(String.class);
		ServiceLoader<FormBindingConverter> serviceLoader = ServiceLoader.load(FormBindingConverter.class);
		for (FormBindingConverter converter : serviceLoader) {
			System.out.println("FormBindingConverter Registered:" + converter.getClass().getSimpleName());
			Converter delegatingConverter = new Converter() {
				@Override
				public <T> T convert(Class<T> type, Object value) {
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

	static Set<Field> extractBoundFields(Class<?> clazz) {
		Set<Field> boundFields = getAllFields(clazz, withAnnotation(FormBindingParam.class));
		if (boundFields.size() == 0) {
			boundFields = getAllFields(clazz);
		}
		Set<Field> transientFields = getAllFields(clazz, withAnnotation(FormBindingTransient.class));
		boundFields.removeAll(transientFields);
		return boundFields;
	}

	static String extractKey(Field field) {
		FormBindingParam formBindingParam = field.getAnnotation(FormBindingParam.class);
		String key = null;
		if (formBindingParam != null) {
			key = formBindingParam.paramName();
			key = key.trim();
			if (key.equals("")) {
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
}
