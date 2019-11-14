package com.github.exabrial.formbinding.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.github.exabrial.formbinding.FormBindingReader;
import com.github.exabrial.formbinding.FormBindingWriter;

public final class FormBinding {
	private static final ServiceLoader<FormBindingReader> formBindingReaderServices;
	private static final ServiceLoader<FormBindingWriter> formBindingWriterServices;

	static {
		formBindingReaderServices = ServiceLoader.load(FormBindingReader.class);
		formBindingWriterServices = ServiceLoader.load(FormBindingWriter.class);
	}

	public static FormBindingReader getReader() {
		FormBindingReader formBindingReader = loadFirstReader();
		if (formBindingReader == null) {
			throw new RuntimeException("The Java SPI system could not locate a FormBindingReader implementation");
		} else {
			return formBindingReader;
		}
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

	public static FormBindingWriter getWriter() {
		FormBindingWriter formBindingWriter = loadFirstWriter();
		if (formBindingWriter == null) {
			throw new RuntimeException("The Java SPI system could not locate a FormBindingWriter implementation");
		} else {
			return formBindingWriter;
		}
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

}