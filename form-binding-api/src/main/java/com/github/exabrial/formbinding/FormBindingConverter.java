package com.github.exabrial.formbinding;

import org.apache.commons.beanutils.Converter;

/**
 * Classes using this interface with be discovered with the Java SPI system.
 * Create your implementation, put it on the classpath, and register it using
 * the SPI system so it can be discovered.
 *
 * Your converter needs to be able to convert TO and FROM the targetclass.
 */
public interface FormBindingConverter extends Converter {
	/**
	 * The class you wish to register use of this converter for. Your converter
	 * should be able to convert your object to a string and back.
	 */
	Class<?> targetClass();
}
