package com.github.exabrial.formbinding.spi.model;

public class TestField {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TestField [value=" + value + "]";
	}
}
