package com.github.exabrial.formbinding.jaxrs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.jaxrs.testmodel.TestObject;

class FormBindingMessageBodyWriterTest {
	private FormBindingMessageBodyWriter<TestObject> writer = new FormBindingMessageBodyWriter<>();

	@Test
	void test() throws WebApplicationException, IOException {
		ByteArrayOutputStream entityStream = new ByteArrayOutputStream();
		TestObject testObject = new TestObject();
		testObject.setAnInt(42);
		testObject.setKey("AGreatSuccess");
		writer.writeTo(testObject, null, null, null, null, null, entityStream);
		assertEquals("anInt=42&key=AGreatSuccess", new String(entityStream.toByteArray()));
	}
}
