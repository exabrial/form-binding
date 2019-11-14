package com.github.exabrial.formbinding.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.test.model.ComplexObject;
import com.github.exabrial.formbinding.test.model.MixedAnnotations;
import com.github.exabrial.formbinding.test.model.NoAnnotations;
import com.github.exabrial.formbinding.test.model.NoAnnotationsWithTransient;
import com.github.exabrial.formbinding.test.model.VeryComplexObject;

class DefaultFormBindingWriterTest {
	private DefaultFormBindingWriter writer = new DefaultFormBindingWriter();

	@Test
	void testRead_null() {
		assertNull(writer.write(null));
	}

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

	@Test
	void testWrite_ComplexObject() {
		String value = writer.write(new ComplexObject());
		assertEquals("intValue=42&mix=MixedAnnotations+%5BheresAField%3DHere%27s+a+field%21%21%21%2C"
				+ "+ignoreMe%3DDon%27t+encode+this%21%21%2C+heresANonAnnotatedField%3DIt+shouldn%27t+be+included%5D&value", value);
	}

	@Test
	void testWrite_VeryComplexObject() {
		VeryComplexObject object = new VeryComplexObject();
		object.testInt = -1;
		object.wrapperLong = 42;
		object.bigDecimal = BigDecimal.valueOf(1234L);
		object.testDouble = 4.2;
		object.stringParam = "testParam";
		object.ignored = true;
		String value = writer.write(object);
		assertEquals("stringParam=testParam&testInt=-1&wrapperLong=42&bigDecimal=1234&testDouble=4.2", value);
	}
}
