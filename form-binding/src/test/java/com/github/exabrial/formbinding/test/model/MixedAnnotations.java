package com.github.exabrial.formbinding.test.model;

import com.github.exabrial.formbinding.FormBindingParam;
import com.github.exabrial.formbinding.FormBindingTransient;

public class MixedAnnotations {
	@FormBindingParam(paramName = "differentName")
	private String heresAField = "Here's a field!!!";
	@FormBindingTransient
	private String ignoreMe = "Don't encode this!!";
	private String heresANonAnnotatedField = "It shouldn't be included";

	public String getHeresAField() {
		return heresAField;
	}

	public void setHeresAField(String heresAField) {
		this.heresAField = heresAField;
	}

	public String getIgnoreMe() {
		return ignoreMe;
	}

	public void setIgnoreMe(String ignoreMe) {
		this.ignoreMe = ignoreMe;
	}

	public String getHeresANonAnnotatedField() {
		return heresANonAnnotatedField;
	}

	public void setHeresANonAnnotatedField(String heresANonAnnotatedField) {
		this.heresANonAnnotatedField = heresANonAnnotatedField;
	}

	@Override
	public String toString() {
		return "MixedAnnotations [heresAField=" + heresAField + ", ignoreMe=" + ignoreMe + ", heresANonAnnotatedField="
				+ heresANonAnnotatedField + "]";
	}
}
