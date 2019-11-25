package com.github.exabrial.formbinding.jaxrs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.jaxrs.testmodel.TestObject;

class FormBindingMessageBodyReaderTest {
	private FormBindingMessageBodyReader<TestObject> reader = new FormBindingMessageBodyReader<>();

	@Test
	void test() throws WebApplicationException, IOException {
		String originalString = "key=AGreatSuccess&anInt=42";
		InputStream entityStream = new ByteArrayInputStream(originalString.getBytes());
		TestObject testObject = reader.readFrom(TestObject.class, null, null, null, null, entityStream);
		assertEquals("AGreatSuccess", testObject.getKey());
		assertEquals(42, testObject.getAnInt());
	}
}
