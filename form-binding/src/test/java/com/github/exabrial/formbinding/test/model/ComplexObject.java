package com.github.exabrial.formbinding.test.model;

import com.github.exabrial.formbinding.FormBindingTransient;

public class ComplexObject {
	private MixedAnnotations mix = new MixedAnnotations();
	private int intValue = 42;
	private String value = null;
	@FormBindingTransient
	private NoAnnotationsWithTransient ignore;

	@Override
	public String toString() {
		return "ComplexObject [mix=" + mix + ", intValue=" + intValue + ", value=" + value + ", ignore=" + ignore + "]";
	}
}
