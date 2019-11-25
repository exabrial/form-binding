package com.github.exabrial.formbinding.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.github.exabrial.formbinding.FormBindingReader;
import com.github.exabrial.formbinding.spi.FormBinding;

@Provider
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class FormBindingMessageBodyReader<T> implements MessageBodyReader<T> {
	private static final FormBindingReader reader = FormBinding.getReader();

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (type.isAssignableFrom(Form.class) || type.isAssignableFrom(Collections.class)
				|| !mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		String input;
		try (Scanner scanner = new Scanner(entityStream, StandardCharsets.UTF_8.name())) {
			input = scanner.useDelimiter("\\A").next();
		}
		return reader.read(input, type);
	}
}
