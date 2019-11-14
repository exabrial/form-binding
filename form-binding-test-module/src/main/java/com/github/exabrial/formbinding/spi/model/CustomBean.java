package com.github.exabrial.formbinding.spi.model;

public class CustomBean {
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "CustomBean [field=" + field + "]";
	}
}
