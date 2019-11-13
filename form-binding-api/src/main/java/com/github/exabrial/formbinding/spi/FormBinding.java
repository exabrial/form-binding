package com.github.exabrial.formbinding.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.github.exabrial.formbinding.FormBindingReader;
import com.github.exabrial.formbinding.FormBindingWriter;

public final class FormBinding {
	private static final String DEFAULT_READER = "com.github.exabrial.formbinding.impl.DefaultFormBindingReader";
	private static final String DEFAULT_WRITER = "com.github.exabrial.formbinding.impl.DefaultFormBindingWriter";
	private static final ServiceLoader<FormBindingReader> formBindingReaderServices;
	private static final ServiceLoader<FormBindingWriter> formBindingWriterServices;

	static {
		formBindingReaderServices = ServiceLoader.load(FormBindingReader.class);
		formBindingWriterServices = ServiceLoader.load(FormBindingWriter.class);
	}

	public static FormBindingReader getReader() {
		FormBindingReader formBindingReader = loadFirstReader();
		if (formBindingReader == null) {
			formBindingReader = forceDefaultReader();
		}
		return formBindingReader;
	}

	public static FormBindingReader getReader(String providerName) {
		for (FormBindingReader formBindingReader : formBindingReaderServices) {
			if (formBindingReader.getClass().getName().equals(providerName)) {
				return formBindingReader;
			}
		}
		throw new RuntimeException("FormBinding provider " + providerName + " not found");
	}

	private static FormBindingReader loadFirstReader() {
		Iterator<FormBindingReader> iterator = formBindingReaderServices.iterator();
		FormBindingReader formBindingReader;
		if (iterator.hasNext()) {
			formBindingReader = iterator.next();
		} else {
			formBindingReader = null;
		}
		return formBindingReader;
	}

	private static FormBindingReader forceDefaultReader() {
		try {
			Class<?> clazz = Class.forName(DEFAULT_WRITER);
			return (FormBindingReader) clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("FormBinding provider " + DEFAULT_READER + " not found", cnfe);
		} catch (Exception exception) {
			throw new RuntimeException("FormBinding provider " + DEFAULT_READER + " could not be instantiated: " + exception, exception);
		}
	}

	public static FormBindingWriter getWriter() {
		FormBindingWriter formBindingWriter = loadFirstWriter();
		if (formBindingWriter == null) {
			formBindingWriter = forceDefaultWriter();
		}
		return formBindingWriter;
	}

	public static FormBindingWriter getWriter(String providerName) {
		for (FormBindingWriter formBindingWriter : formBindingWriterServices) {
			if (formBindingWriter.getClass().getName().equals(providerName)) {
				return formBindingWriter;
			}
		}
		throw new RuntimeException("FormBinding provider " + providerName + " not found");
	}

	private static FormBindingWriter loadFirstWriter() {
		Iterator<FormBindingWriter> iterator = formBindingWriterServices.iterator();
		FormBindingWriter formBindingWriter;
		if (iterator.hasNext()) {
			formBindingWriter = iterator.next();
		} else {
			formBindingWriter = null;
		}
		return formBindingWriter;
	}

	private static FormBindingWriter forceDefaultWriter() {
		try {
			Class<?> clazz = Class.forName(DEFAULT_WRITER);
			return (FormBindingWriter) clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("FormBinding provider " + DEFAULT_WRITER + " not found", cnfe);
		} catch (Exception exception) {
			throw new RuntimeException("FormBinding provider " + DEFAULT_WRITER + " could not be instantiated: " + exception, exception);
		}
	}
}