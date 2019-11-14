package com.github.exabrial.formbinding.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.test.model.MixedAnnotations;

class DefaultFormBindingReaderTest {
	private DefaultFormBindingReader reader = new DefaultFormBindingReader();

	@Test
	void testRead_mixedAnnnotations() {
		String payload = "ThisIsATest";
		String input = "differentName=" + payload;
		MixedAnnotations mixedAnnotations = reader.read(input, MixedAnnotations.class);
		assertEquals(payload, mixedAnnotations.getHeresAField());
	}
}
