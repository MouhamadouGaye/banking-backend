package com.mgaye.banking_backend.util;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerResponse.Context;

// util/PdfGenerator.java
@Service
public class PdfGenerator {

    private final TemplateEngine templateEngine;
    private final PdfFontProvider fontProvider;

    public PdfGenerator() {
        this.templateEngine = new TemplateEngine();
        this.fontProvider = new PdfFontProvider();
        configureTemplateEngine();
    }

    private void configureTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);
    }

    public byte[] generatePdf(StatementData data) throws IOException {
        Context context = new Context();
        context.setVariable("statement", data);

        String html = templateEngine.process("account-statement", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            ConverterProperties props = new ConverterProperties();
            props.setFontProvider(fontProvider);

            HtmlConverter.convertToPdf(html, pdf, props);
            return outputStream.toByteArray();
        }
    }

    // Inner class for font handling
    private static class PdfFontProvider extends DefaultFontProvider {
        public PdfFontProvider() {
            addFont("fonts/arial.ttf");
        }
    }
}