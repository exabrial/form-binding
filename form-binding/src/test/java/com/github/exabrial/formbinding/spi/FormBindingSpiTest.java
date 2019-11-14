package com.github.exabrial.formbinding.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.exabrial.formbinding.FormBindingReader;
import com.github.exabrial.formbinding.FormBindingWriter;
import com.github.exabrial.formbinding.spi.model.CustomBean;
import com.github.exabrial.formbinding.test.model.SpiCelebration;

class FormBindingSpiTest {
	private FormBindingWriter writer = FormBinding.getWriter();
	private FormBindingReader reader = FormBinding.getReader();

	@Test
	void testSpi_writer() {
		String spiWorks = "spiWorks=true";
		String output = writer.write(new SpiCelebration());
		assertEquals(spiWorks, output);
		System.out.println(output);
	}

	@Test
	void testSpi_reader() {
		String testPayload = "yeeyee";
		SpiCelebration spiCelebration = reader.read("spiWorks=" + testPayload, SpiCelebration.class);
		assertEquals(testPayload, spiCelebration.getSpiWorks());
		System.out.println(spiCelebration);
	}

	@Test
	void testSpi_FormBindingConverter_writer() {
		CustomBean customBean = new CustomBean();
		String testPayload = "this is the test payload";
		customBean.setField(testPayload);
		String writerOutput = writer.write(customBean);
		assertEquals("field=this+is+the+test+payloadWHOOMPITWORKS", writerOutput);
	}

	@Test
	void testSpi_FormBindingConverter_reader() {
		CustomBean read = reader.read("arbitrary input", CustomBean.class);
		assertEquals("this was set by the converter", read.getField());
	}
}
