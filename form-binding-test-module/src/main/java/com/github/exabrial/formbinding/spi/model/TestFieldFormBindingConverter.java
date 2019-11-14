package com.github.exabrial.formbinding.spi.model;

import com.github.exabrial.formbinding.FormBindingConverter;

public class TestFieldFormBindingConverter implements FormBindingConverter {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Class<T> type, Object value) {
		if (type.equals(String.class) && value.getClass().equals(targetClass())) {
			TestField testField = (TestField) value;
			return (T) (testField.getValue() + "WHOOMPITWORKS");
		} else if (type.equals(targetClass()) && value.getClass().equals(String.class)) {
			TestField testField = new TestField();
			testField.setValue("this was set by the converter");
			return (T) testField;
		} else {
			return null;
		}
	}

	@Override
	public Class<TestField> targetClass() {
		return TestField.class;
	}
}
