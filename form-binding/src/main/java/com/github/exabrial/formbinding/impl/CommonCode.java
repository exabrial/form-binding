package com.github.exabrial.formbinding.impl;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtilsBean;

import com.github.exabrial.formbinding.FormBindingConverter;
import com.github.exabrial.formbinding.FormBindingParam;
import com.github.exabrial.formbinding.FormBindingTransient;

@SuppressWarnings("unchecked")
class CommonCode {
	private static final Pattern JAVA_FIELD_PATTERN = Pattern.compile("^[a-z_][a-z0-9_-]*$", Pattern.CASE_INSENSITIVE);

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

	static void loadSpiConverters(ConvertUtilsBean cub) {
		ServiceLoader<FormBindingConverter> serviceLoader = ServiceLoader.load(FormBindingConverter.class);
		for (FormBindingConverter converter : serviceLoader) {
			cub.register(converter, converter.targetClass());
		}
	}
}
