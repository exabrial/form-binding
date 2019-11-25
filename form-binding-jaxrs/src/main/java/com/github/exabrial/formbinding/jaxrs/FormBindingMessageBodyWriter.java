package com.github.exabrial.formbinding.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.github.exabrial.formbinding.FormBindingWriter;
import com.github.exabrial.formbinding.spi.FormBinding;

@Provider
@Produces(MediaType.APPLICATION_FORM_URLENCODED)
public class FormBindingMessageBodyWriter<T> implements MessageBodyWriter<T> {
	private static final FormBindingWriter writer = FormBinding.getWriter();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (type.isAssignableFrom(Form.class) || type.isAssignableFrom(Collections.class)
				|| !mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		try (PrintWriter pw = new PrintWriter(entityStream)) {
			String form = writer.write(t);
			pw.write(form);
		}
	}
}
