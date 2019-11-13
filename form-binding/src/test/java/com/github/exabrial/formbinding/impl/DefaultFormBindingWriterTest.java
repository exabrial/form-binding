package com.github.exabrial.formbinding.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.test.model.MixedAnnotations;
import com.github.exabrial.formbinding.test.model.NoAnnotations;
import com.github.exabrial.formbinding.test.model.NoAnnotationsWithTransient;

class DefaultFormBindingWriterTest {
	private DefaultFormBindingWriter writer = new DefaultFormBindingWriter();

	@Test
	void testWrite_MixedAnnotations() {
		String value = writer.write(new MixedAnnotations());
		assertEquals("differentName=Here%27s+a+field%21%21%21", value);
	}

	@Test
	void testWrite_NoAnnotations() {
		String value = writer.write(new NoAnnotations());
		assertEquals("heresAField2=This+should+be+included2&heresAField=This+should+be+included", value);
	}

	@Test
	void testWrite_NoAnnotationsWithTransient() {
		String value = writer.write(new NoAnnotationsWithTransient());
		assertEquals("heresAField2=This+should+be+included2&heresAField=This+should+be+included", value);
	}
}
