package com.github.exabrial.formbinding.spi.model;

import com.github.exabrial.formbinding.FormBindingParam;

public class CustomBean {
	@FormBindingParam
	private TestField testField;

	public TestField getTestField() {
		return testField;
	}

	public void setTestField(final TestField testField) {
		this.testField = testField;
	}
}
