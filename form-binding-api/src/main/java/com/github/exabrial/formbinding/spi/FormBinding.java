package com.github.exabrial.formbinding.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.github.exabrial.formbinding.FormBindingReader;
import com.github.exabrial.formbinding.FormBindingWriter;

public final class FormBinding {
	public static FormBindingReader getReader() {
		return getReader(null);
	}

	public static FormBindingReader getReader(final ClassLoader classLoader) {
		final Iterator<FormBindingReader> iterator = ServiceLoader.load(FormBindingReader.class, classLoader).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new RuntimeException("The Java SPI system could not locate a FormBindingReader implementation");
		}
	}

	public static FormBindingReader getReaderByName(final String providerName) {
		return getReaderByName(providerName, null);
	}

	public static FormBindingReader getReaderByName(final String providerName, final ClassLoader classLoader) {
		for (final FormBindingReader formBindingReader : ServiceLoader.load(FormBindingReader.class, classLoader)) {
			if (formBindingReader.getClass().getName().equals(providerName)) {
				return formBindingReader;
			}
		}
		throw new RuntimeException("FormBinding provider " + providerName + " not found");
	}

	public static FormBindingWriter getWriter() {
		return getWriter(null);
	}

	public static FormBindingWriter getWriter(final ClassLoader classLoader) {
		final Iterator<FormBindingWriter> iterator = ServiceLoader.load(FormBindingWriter.class, classLoader).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new RuntimeException("The Java SPI system could not locate a FormBindingWriter implementation");
		}
	}

	public static FormBindingWriter getWriterByName(final String providerName) {
		return getWriterByName(providerName, null);
	}

	public static FormBindingWriter getWriterByName(final String providerName, final ClassLoader classLoader) {
		for (final FormBindingWriter formBindingWriter : ServiceLoader.load(FormBindingWriter.class, classLoader)) {
			if (formBindingWriter.getClass().getName().equals(providerName)) {
				return formBindingWriter;
			}
		}
		throw new RuntimeException("FormBinding provider " + providerName + " not found");
	}
}
