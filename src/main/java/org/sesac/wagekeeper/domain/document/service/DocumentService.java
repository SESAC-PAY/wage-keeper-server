package org.sesac.wagekeeper.domain.document.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RequiredArgsConstructor
@Service
@Transactional
public class DocumentService {

    public File convertHtmltoPdf(String html) throws IOException {
        String htmlWithEncoding = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head>" + html + "</html>";

        ConverterProperties properties = new ConverterProperties();
        properties.setCharset("UTF-8");

        DefaultFontProvider fontProvider = new DefaultFontProvider(true, true, true);
        fontProvider.addFont("src/main/resources/static/fonts/malgunbd.ttf", PdfEncodings.IDENTITY_H);
        properties.setFontProvider(fontProvider);

        File pdfFile = File.createTempFile("document", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            HtmlConverter.convertToPdf(htmlWithEncoding, fos, properties);
        }

        return pdfFile;
    }


}
