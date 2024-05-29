package com.github.exabrial.formbinding.test.model;

import com.github.exabrial.formbinding.FormBindingTransient;

public class ComplexObject {
	private final MixedAnnotations mix = new MixedAnnotations();
	private final int intValue = 42;
	private final String nullValue = null;
	@FormBindingTransient
	private NoAnnotationsWithTransient ignore;

	@Override
	public String toString() {
		return "ComplexObject [mix=" + mix + ", intValue=" + intValue + ", nullValue=" + nullValue + ", ignore=" + ignore + "]";
	}
}
