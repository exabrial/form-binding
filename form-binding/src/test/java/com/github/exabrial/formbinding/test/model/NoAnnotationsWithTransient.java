package com.github.exabrial.formbinding.test.model;

import com.github.exabrial.formbinding.FormBindingTransient;

public class NoAnnotationsWithTransient {
	private String heresAField = "This should be included";
	private String heresAField2 = "This should be included2";
	@FormBindingTransient
	private String excludedField = "This should be excluded";

	@Override
	public String toString() {
		return "NoAnnotationsWithTransient [heresAField=" + heresAField + ", heresAField2=" + heresAField2 + ", excludedField="
				+ excludedField + "]";
	}
}
