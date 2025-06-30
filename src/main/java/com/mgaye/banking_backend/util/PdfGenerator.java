package com.mgaye.banking_backend.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.mgaye.banking_backend.dto.StatementData;

// iText imports
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;

// util/PdfGenerator.java
@Service
public class PdfGenerator {

    private final TemplateEngine templateEngine;
    private final FontProvider fontProvider;

    public PdfGenerator() {
        this.templateEngine = new TemplateEngine();
        this.fontProvider = new FontProvider();
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
    // Optionally, add custom fonts if needed
    // fontProvider.addFont("fonts/arial.ttf");
}