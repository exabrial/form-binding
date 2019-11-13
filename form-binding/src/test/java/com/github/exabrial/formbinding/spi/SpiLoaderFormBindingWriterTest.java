package com.github.exabrial.formbinding.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.FormBindingWriter;
import com.github.exabrial.formbinding.test.model.SpiCelebration;

class SpiLoaderFormBindingWriterTest {

	@Test
	void testSpi() {
		FormBindingWriter writer = FormBinding.getWriter();
		String output = writer.write(new SpiCelebration());
		assertEquals("spiWorks=true", output);
		System.out.println(output);
	}
}
