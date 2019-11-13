package com.github.exabrial.formbinding;

public interface FormBindingReader {
	<ReturnType> ReturnType read(String input, Class<ReturnType> returnTypeClazz);
}
