package com.github.exabrial.formbinding.test.model;

public class NoAnnotations {
	private String heresAField = "This should be included";
	private String heresAField2 = "This should be included2";

	@Override
	public String toString() {
		return "NoAnnotations [heresAField=" + heresAField + ", heresAField2=" + heresAField2 + "]";
	}
}
