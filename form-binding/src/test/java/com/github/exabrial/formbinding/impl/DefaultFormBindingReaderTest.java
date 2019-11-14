package com.github.exabrial.formbinding.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.test.model.MixedAnnotations;
import com.github.exabrial.formbinding.test.model.VeryComplexObject;

class DefaultFormBindingReaderTest {
	private DefaultFormBindingReader reader = new DefaultFormBindingReader();

	@Test
	void testRead_null() {
		assertNull(reader.read(null, MixedAnnotations.class));
		assertNull(reader.read("a=b", null));
	}

	@Test
	void testRead_empty() {
		assertNotNull(reader.read("", MixedAnnotations.class));
	}

	@Test
	void testRead_VeryComplexObject() {
		VeryComplexObject object = new VeryComplexObject();
		object.testInt = -1;
		object.wrapperLong = 42;
		object.bigDecimal = BigDecimal.valueOf(1234L);
		object.testDouble = 4.2;
		object.stringParam = "testParam";
		VeryComplexObject readValue = reader.read(
				"ignored=true&date=Wed+Dec+31+18%3A00%3A00+CST+1969&stringParam=testParam&testInt=-1&wrapperLong=42&bigDecimal=1234&testDouble=4.2",
				VeryComplexObject.class);
		assertEquals(object, readValue);
		assertNull(readValue.ignored);
	}
}
