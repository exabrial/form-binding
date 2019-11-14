package com.github.exabrial.formbinding.spi.model;

import com.github.exabrial.formbinding.FormBindingConverter;

public class CustomBeanFormBindingConverter implements FormBindingConverter {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Class<T> type, Object value) {
		if (type.equals(String.class) && value.getClass().equals(targetClass())) {
			CustomBean customBean = (CustomBean) value;
			return (T) (customBean.getField() + "WHOOMPITWORKS");
		} else if (type.equals(targetClass()) && value.getClass().equals(String.class)) {
			CustomBean customBean = new CustomBean();
			customBean.setField("this was set by the converter");
			return (T) customBean;
		} else {
			return null;
		}
	}

	@Override
	public Class<CustomBean> targetClass() {
		return CustomBean.class;
	}
}
